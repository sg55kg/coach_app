import * as devalue from 'devalue'
import {Auth0Client, createAuth0Client, User} from "@auth0/auth0-spa-js";
import {auth0Client, isAuthenticated, user, loadingAuth, userDB} from "../stores/authStore";
import Cookies from "js-cookie";



export default class UserService {

    static initializeAuth0Client = async () => {
        loadingAuth.set(true)
        let config = {
            "domain": "dev-iubbkos4gue16ad5.us.auth0.com",
            "clientId": "iVM0PNhPw0acBMwrb5IUyedtMjg7ejlQ",
        }

        const client = await createAuth0Client({
            domain: config.domain,
            clientId: config.clientId,
            authorizationParams: {
                audience: 'http://localhost:8180',
            }
        })

        auth0Client.set(client)
        isAuthenticated.set(await client.isAuthenticated())
        const data = await client.getUser()
        user.set(data as User)
        if (data !== undefined) {
            await UserService.fetchUserData(client, data.email as string)
        }

        loadingAuth.set(false)
    }

    static loginWithPopUp = async (client: Auth0Client) => {
        if (!client) return

        try {
            await client.loginWithPopup()
            const data = await client.getUser()
            user.set(data)
            isAuthenticated.set(await client.isAuthenticated())
            await UserService.fetchUserData(client, data!.email as string)
        } catch (e) {
            console.log(e)
        }

    }

    static logout = async (client: Auth0Client) => {
        if (!client) return

        await client.logout({
            logoutParams: {
                returnTo: 'https://localhost:5173'
            }
        })
    }

    static fetchUserData = async (client: Auth0Client, email: string) => {
        const token = await client.getTokenSilently()
        let res = await fetch(`http://localhost:8180/api/users/${email}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
        if (res.status === 404) {
            const fields = await client.getUser()


            res = await fetch(`http://localhost:8180/api/users`, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({ email: fields!.email, name: fields!.name })
            })
        }
        const userData = await res.json()

        if (!userData.coachData) {
            res = await fetch(`http://localhost:8180/api/coach`, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ id: null, user: userData })
            })

            userData.coachData = await res.json()
        }

        // if (!userData.athleteData) {
        //     res = await fetch(`http://localhost:8180/api/athlete`, {
        //         method: 'POST',
        //         headers: {
        //             'Authorization': 'Bearer ' + token
        //         },
        //         body: JSON.stringify({})
        //     })
        // }
        console.log(userData)
        userDB.set(userData)
    }
}