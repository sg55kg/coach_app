
import type {Actions, PageServerLoad} from "../../.svelte-kit/types/src/routes/sverdle/$types";

export const load: PageServerLoad = ({ cookies }) => {

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