
import type {Actions, PageServerLoad} from "../../.svelte-kit/types/src/routes/sverdle/$types";
import jwtDecode from "jwt-decode";
import {authUser, userDB} from "$lib/stores/authStore";
import type {RequestHandler} from "@sveltejs/kit";


const randomString = (length: number, chars: string) => {
    let result = '';
    for (let i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
}
const state: string = randomString(32, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');


export const load: PageServerLoad = async ({ cookies, params, url }) => {
    const code = url.searchParams.get('code')
    const encoded = state
    if (!code && !cookies.get('accessToken')) {
        return { state: encoded }
    }
    if (!cookies.get('accessToken')) {
        const options = {
            "grant_type": "authorization_code",
            "client_id": import.meta.env.VITE_AUTH0_CLIENT_ID,
            "client_secret": import.meta.env.VITE_AUTH0_CLIENT_SECRET,
            "code": code,
            "redirect_uri": import.meta.env.VITE_REDIRECT_URI,
            "audience": import.meta.env.VITE_AUTH0_AUDIENCE,
        }
        console.log(options)
        try {
            const res = await fetch(`https://dev-iubbkos4gue16ad5.us.auth0.com/oauth/token`, {
                method: 'POST',
                headers: {'content-type': 'application/json'},
                body: JSON.stringify(options)
            })
            console.log(res.status)
            //console.log()
            const data = await res.json()
            //console.log(data)
            console.log(jwtDecode(data['access_token']))
            cookies.set('accessToken', data['access_token'], { httpOnly: true })
            const user = jwtDecode(data['id_token']) as any
            const accessToken = data['access_token']

            const userRes = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/${user.email}`, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + accessToken,
                    'Content-Type': 'application/json',
                }
            })

            // console.log(userRes.statusText)
            const userData = await userRes.json()

            return {
                user,
                userData
            }
        } catch (e) {
            console.log(e)
        }
    }

    return {

    };
};



// export const actions: Actions = {
//     /**
//      * Modify game state in reaction to a keypress. If client-side JavaScript
//      * is available, this will happen in the browser instead of here
//      */
//     update: async ({ request, cookies }) => {
//         const game = new Game(cookies.get('sverdle'));
//
//         const data = await request.formData();
//         const key = data.get('key');
//
//         const i = game.answers.length;
//
//         if (key === 'backspace') {
//             game.guesses[i] = game.guesses[i].slice(0, -1);
//         } else {
//             game.guesses[i] += key;
//         }
//
//         cookies.set('sverdle', game.toString());
//     },
//
//     /**
//      * Modify game state in reaction to a guessed word. This logic always runs on
//      * the server, so that people can't cheat by peeking at the JavaScript
//      */
//     enter: async ({ request, cookies }) => {
//         const game = new Game(cookies.get('sverdle'));
//
//         const data = await request.formData();
//         const guess = data.getAll('guess')  as string[];
//
//         if (!game.enter(guess)) {
//             return invalid(400, { badGuess: true });
//         }
//
//         cookies.set('sverdle', game.toString());
//     },
//
//     restart: async ({ cookies }) => {
//         cookies.delete('sverdle');
//     }
// };
//
// class Game {
//     index: number;
//     guesses: string[];
//     answers: string[];
//     answer: string;
//
//     /**
//      * Create a game object from the player's cookie, or initialise a new game
//      */
//     constructor(serialized: string | undefined) {
//         if (serialized) {
//             const [index, guesses, answers] = serialized.split('-');
//
//             this.index = +index;
//             this.guesses = guesses ? guesses.split(' ') : [];
//             this.answers = answers ? answers.split(' ') : [];
//         } else {
//             this.index = Math.floor(Math.random() * words.length);
//             this.guesses = ['', '', '', '', '', ''];
//             this.answers = [] ;
//         }
//
//         this.answer = words[this.index];
//     }
//
//     /**
//      * Update game state based on a guess of a five-letter word. Returns
//      * true if the guess was valid, false otherwise
//      */
//     enter(letters: string[]) {
//         const word = letters.join('');
//         const valid = allowed.has(word);
//
//         if (!valid) return false;
//
//         this.guesses[this.answers.length] = word;
//
//         const available = Array.from(this.answer);
//         const answer = Array(5).fill('_');
//
//         // first, find exact matches
//         for (let i = 0; i < 5; i += 1) {
//             if (letters[i] === available[i]) {
//                 answer[i] = 'x';
//                 available[i] = ' ';
//             }
//         }
//
//         // then find close matches (this has to happen
//         // in a second step, otherwise an early close
//         // match can prevent a later exact match)
//         for (let i = 0; i < 5; i += 1) {
//             if (answer[i] === '_') {
//                 const index = available.indexOf(letters[i]);
//                 if (index !== -1) {
//                     answer[i] = 'c';
//                     available[index] = ' ';
//                 }
//             }
//         }
//
//         this.answers.push(answer.join(''));
//
//         return true;
//     }
//
//     /**
//      * Serialize game state so it can be set as a cookie
//      */
//     toString() {
//         return `${this.index}-${this.guesses.join(' ')}-${this.answers.join(' ')}`;
//     }
// }