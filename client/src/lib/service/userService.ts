import * as devalue from 'devalue'
import {Auth0Client, createAuth0Client, User as Auth0User} from "@auth0/auth0-spa-js";
import {auth0Client, isAuthenticated, user, loadingAuth, userDB} from "../stores/authStore";
import {AthleteData, type AthleteDataDTO, AthleteRecord, User} from "$lib/classes/user";
import {redirect} from "@sveltejs/kit";
import {goto} from "$app/navigation";



export default class UserService {

    static initializeAuth0Client = async () => {
        loadingAuth.set(true)
        let client
        try {
            let config = {
                "domain": import.meta.env.VITE_AUTH0_DOMAIN,
                "clientId": import.meta.env.VITE_AUTH0_CLIENT_ID,
            }

            client = await createAuth0Client({
                domain: config.domain,
                clientId: config.clientId,
                authorizationParams: {
                    audience: import.meta.env.VITE_AUTH0_AUDIENCE,
                }
            })
            isAuthenticated.set(await client.isAuthenticated())
            const data = await client.getUser()
            user.set(data as Auth0User)
            console.log(data)
            if (data !== undefined) {
                await UserService.initializeUserData(client, data)
            } else {
                userDB.set(null)
            }
        } catch (e) {
            console.log(e)
        }
        loadingAuth.set(false)
        return client
    }

    static initializeUserData = async (client: Auth0Client, data: Auth0User) => {
        const userData = await UserService.fetchUserData(client, data.email as string)
        if (!userData) {
            console.log('No user DB')
            window.sessionStorage.setItem('lastPath', window.location.pathname)
            if (window.location.pathname !== '/') {
                window.location.replace('/')
            }
        } else {
            console.log ('userDB Found', window.sessionStorage.getItem('lastPath'))
            userDB.set(userData)
            const path = window.sessionStorage.getItem('lastPath')
            if (path) {
                window.sessionStorage.removeItem('lastPath')
                console.log('found path')
                await goto(path)
            }
        }
    }

    static loginWithPopUp = async (client: Auth0Client) => {
        if (!client) return

        try {
            await client.loginWithPopup()
            const data = await client.getUser()
            if (!data) {
                throw new Error('Could not log in')
            }
            user.set(data)
            isAuthenticated.set(await client.isAuthenticated())
            await UserService.initializeUserData(client, data)
        } catch (e) {
            throw new Error('Could not log in')
        }

    }

    static loginWithRedirect = async (client: Auth0Client) => {
        try {
            await client.loginWithRedirect({ authorizationParams: { redirect_uri: import.meta.env.VITE_REDIRECT_URI}})
        } catch (e) {
            console.log(e)
            //throw new Error('Could not login')
        }
    }

    static logout = async (client: Auth0Client) => {
        if (!client) return
        window.sessionStorage.setItem('lastPath', window.location.pathname)
        userDB.set(null)
        user.set(undefined)
        await client.logout({
            logoutParams: {
                returnTo: window.location.origin
            }
        })
    }

    static fetchUserData = async (client: Auth0Client, email: string) => {
        const token = await client.getTokenSilently()

        try {
            let res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/${email}`, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })

            if (res.status === 404) {
                const fields = await client.getUser()

                res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users`, {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email: fields!.email, name: fields!.name })
                })
            }
            let userData = await res.json()

            console.log('user fetch', userData)

            if (!userData.coachData && res.status !== 500) {
                res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/coach`, {
                    method: 'PUT',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ userId: userData.id, email: userData.email, username: userData.username })
                })

                if (res.status !== 405 && res.status !== 500) {
                    userData = await res.json()
                }

            }

            if (!userData.athleteData && res.status !== 500) {
                res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/athlete`, {
                    method: 'PUT',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ userId: userData.id, name: userData.username })
                })

                if (res.status !== 405 && res.status !== 500) {
                    userData = await res.json()
                }
            }
            if (userData) {
                return User.build(userData)
            }
        } catch (e) {
            console.log(e)
            return null
        }
    }

    static updateAthleteRecords = async (client: Auth0Client, record: AthleteRecord, id: string) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/athletes/${id}/record`, {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + accessToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(record)
        })

        if (res.status > 299) {
            throw new Error('Could not update athlete records')
        }

        return new AthleteRecord(await res.json())
    }

    static updateAthleteData = async (client: Auth0Client, athlete: AthleteData) => {
        const accessToken = await client.getTokenSilently()
        const id = athlete.id

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/athletes/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + accessToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id,
                teamId: athlete.team ? athlete.team.id : null,
                coachId: athlete.coach ? athlete.coach.id : null,
                name: athlete.name,
                records: athlete.records
            })
        })
        if (res.status > 299) {
            throw new Error('Could not update athlete data')
        }
        return AthleteData.createFrom(await res.json() as AthleteDataDTO)
    }
}