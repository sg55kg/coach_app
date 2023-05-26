<script lang="ts">
    import { confetti } from '@neoconfetti/svelte';
    import {allowed, words, Game} from "./words";
    import {userDB} from "$lib/stores/authStore.js";


    let game: Game = new Game()
    let badGuess: boolean = false

    $: won = game.answers.at(-1) === 'xxxxx';

    /** The index of the current guess */
    $: index = won ? -1 : game.answers.length;

    /** Whether the current guess can be submitted */
    $: submittable = game.guesses[index]?.length === 5;

    let showInstructions = false

    /**
     * A map of classnames for all letters that have been guessed,
     * used for styling the keyboard
     */
    let classnames: Record<string, 'exact' | 'close' | 'missing'>;

    $: {
        classnames = {};

        game.answers.forEach((answer, i) => {
            const guess = game.guesses[i];

            for (let i = 0; i < 5; i += 1) {
                const letter = guess[i];

                if (answer[i] === 'x') {
                    classnames[letter] = 'exact';
                } else if (!classnames[letter]) {
                    classnames[letter] = answer[i] === 'c' ? 'close' : 'missing';
                }
            }
        });

        classnames = classnames
    }

    /**
     * Modify the game state without making a trip to the server,
     * if client-side JavaScript is enabled
     */
    function update(event: MouseEvent) {
        const guess = game.guesses[index];
        const key = (event.target as HTMLButtonElement).getAttribute(
            'data-key'
        );

        if (key === 'backspace') {
            game.guesses[index] = guess.slice(0, -1);
            badGuess = false;
        } else if (guess.length < 5) {
            game.guesses[index] += key;
        }
    }

    /**
     * Trigger form logic in response to a keydown event, so that
     * desktop users can use the keyboard to play the game
     */
    function keydown(event: KeyboardEvent) {
        if (event.metaKey) return;

        const i = game.answers.length;

        document
            .querySelector(`[data-key="${event.key.toLowerCase()}" i]`)
            ?.dispatchEvent(new MouseEvent('click', { cancelable: true }));
    }

        /**
         * Modify game state in reaction to a guessed word. This logic always runs on
         * the server, so that people can't cheat by peeking at the JavaScript
         */
    function enter(guess: string[]) {
        if (!game.enter(guess)) {
            return badGuess = true
        }
        index = game.answers.length

            game.answers.forEach((answer, i) => {
                const guess = game.guesses[i];

                for (let i = 0; i < 5; i += 1) {
                    const letter = guess[i];

                    if (answer[i] === 'x') {
                        classnames[letter] = 'exact';
                    } else if (!classnames[letter]) {
                        classnames[letter] = answer[i] === 'c' ? 'close' : 'missing';
                    }
                }
            });
            won = game.answers.at(-1) === 'xxxxx'
    }

    function restart() {
        game = new Game()
    }
</script>

<svelte:window on:keydown={keydown} />

<svelte:head>
    <title>Error</title>
    <meta name="description" content="A Wordle clone written in SvelteKit" />
</svelte:head>

<div class="flex flex-col items-center justify-center text-center">
    <div class="my-4">
        <h1 class="text-3xl font-bold">Uh oh! Something went wrong</h1>
        <p class="text-xl font-bold">If you're seeing this page, an error on our end has occurred. While you're here, why not play some Wordle? </p>
        <a class="text-link hover:text-link-shade" href={$userDB ? '/home' : '/'}>Return to home</a>
    </div>
    <div class="grid" class:playing={!won} class:bad-guess={badGuess}>
        {#each Array(6) as _, row}

            <div class={`row ${index === row ? 'current' : ''}`}>
                {#each Array(5) as _, column}
                    {@const answer = game.answers[row]?.[column]}

                    <input
                            name="guess"
                            disabled={row !== index}
                            readonly
                            class:exact={answer === 'x'}
                            class:close={answer === 'c'}
                            aria-selected={row === index && column === game.guesses[row].length}
                            value={game.guesses[row]?.[column] ?? ''}
                    />
                {/each}
            </div>
        {/each}
    </div>

    <div class="controls">
        {#if won || game.answers.length >= 6}
            {#if !won && game.answer}
                <p>the answer was "{game.answer}"</p>
            {/if}
            <button data-key="enter" aria-selected="true" class="restart" on:click={restart}>
                {won ? 'you won :)' : `game over :(`} play again?
            </button>
        {:else}
            <div class="keyboard">
                <button data-key="enter" aria-selected={submittable} disabled={!submittable} on:click={() => enter(game.guesses[index])}>enter</button>

                <button
                        on:click={update}
                        data-key="backspace"
                        name="key"
                        value="backspace"
                >
                    back
                </button>

                {#each ['qwertyuiop', 'asdfghjkl', 'zxcvbnm'] as row}
                    <div class="row">
                        {#each row as letter}
                            <button
                                    on:click={update}
                                    data-key={letter}
                                    class={classnames[letter]}
                                    disabled={game.guesses[index].length === 5}
                                    name="key"
                                    value={letter}
                            >
                                {letter}
                            </button>
                        {/each}
                    </div>
                {/each}
            </div>
        {/if}
    </div>
    <button on:click={() => showInstructions = !showInstructions} class="how-to-play mt-4 underline font-bold" href="/sverdle/how-to-play">How to play</button>
</div>
{#if won}
    <div
            style="position: absolute; left: 50%; top: 30%"
            use:confetti={{
			force: 0.7,
			stageWidth: window.innerWidth,
			stageHeight: window.innerHeight,
			colors: ['#ff3e00', '#40b3ff', '#676778']
		}}
    ></div>
{/if}
{#if showInstructions}
    <div class="text-column">
        <h1>How to play Sverdle</h1>

        <p>
            <a href="https://www.nytimes.com/games/wordle/index.html">Wordle</a> is a
            word guessing game. To play, enter a five-letter English word. For example:
        </p>

        <div class="example">
            <span class="close">r</span>
            <span class="missing">i</span>
            <span class="close">t</span>
            <span class="missing">z</span>
            <span class="exact">y</span>
        </div>

        <p>
            The <span class="exact">y</span> is in the right place. <span class="close">r</span> and
            <span class="close">t</span>
            are the right letters, but in the wrong place. The other letters are wrong, and can be discarded.
            Let's make another guess:
        </p>

        <div class="example">
            <span class="exact">p</span>
            <span class="exact">a</span>
            <span class="exact">r</span>
            <span class="exact">t</span>
            <span class="exact">y</span>
        </div>

        <p>This time we guessed right! You have <strong>six</strong> guesses to get the word.</p>
    </div>

    <style>
        span {
            display: inline-flex;
            justify-content: center;
            align-items: center;
            font-size: 0.8em;
            width: 2.4em;
            height: 2.4em;
            background-color: white;
            box-sizing: border-box;
            border-radius: 2px;
            border-width: 2px;
            color: rgba(0, 0, 0, 0.7);
        }

        .missing {
            background: rgba(255, 255, 255, 0.5);
            color: rgba(0, 0, 0, 0.5);
        }

        .close {
            border-style: solid;
            border-color: var(--color-theme-2);
        }

        .exact {
            background: var(--color-theme-2);
            color: white;
        }

        .example {
            display: flex;
            justify-content: flex-start;
            margin: 1rem 0;
            gap: 0.2rem;
        }

        .example span {
            font-size: 1.4rem;
        }

        p span {
            position: relative;
            border-width: 1px;
            border-radius: 1px;
            font-size: 0.4em;
            transform: scale(2) translate(0, -10%);
            margin: 0 1em;
        }
    </style>

{/if}


<style>
    form {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        gap: 1rem;
        flex: 1;
    }

    .how-to-play {
        color: var(--color-text);
    }

    .how-to-play::before {
        content: 'i';
        display: inline-block;
        font-size: 0.8em;
        font-weight: 900;
        width: 1em;
        height: 1em;
        padding: 0.2em;
        line-height: 1;
        border: 1.5px solid var(--color-text);
        border-radius: 50%;
        text-align: center;
        margin: 0 0.5em 0 0;
        position: relative;
        top: -0.05em;
        background-color: dodgerblue;
        color: white;
    }

    .grid {
        --width: min(100vw, 40vh, 380px);
        max-width: var(--width);
        align-self: center;
        justify-self: center;
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
    }

    .grid .row {
        display: grid;
        grid-template-columns: repeat(5, 1fr);
        grid-gap: 0.2rem;
        margin: 0 0 0.2rem 0;
    }

    .grid.bad-guess .row.current {
        animation: wiggle 0.5s;
    }

    .grid.playing .row.current {
        filter: drop-shadow(3px 3px 10px var(--color-bg-0));
    }

    input {
        aspect-ratio: 1;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
        box-sizing: border-box;
        text-transform: lowercase;
        border: none;
        font-size: calc(0.08 * var(--width));
        border-radius: 2px;
        background: white;
        margin: 0;
        color: rgba(0, 0, 0, 0.7);
    }

    input:disabled:not(.exact):not(.close) {
        background: rgba(255, 255, 255, 0.5);
        color: rgba(0, 0, 0, 0.5);
    }

    input.exact {
        background: var(--color-theme-2);
        color: white;
    }

    input.close {
        border: 2px solid var(--color-theme-2);
    }

    input:focus {
        outline: none;
    }

    [aria-selected='true'] {
        outline: 2px solid var(--color-theme-1);
    }

    input:not(:disabled)::selection {
        background: transparent;
        color: var(--color-theme-1);
    }

    .controls {
        text-align: center;
        justify-content: center;
        height: min(18vh, 10rem);
    }

    .keyboard {
        --gap: 0.2rem;
        position: relative;
        display: flex;
        flex-direction: column;
        gap: var(--gap);
        height: 100%;
    }

    .keyboard .row {
        display: flex;
        justify-content: center;
        gap: 0.2rem;
        flex: 1;
    }

    .keyboard button,
    .keyboard button:disabled {
        --size: min(8vw, 4vh, 40px);
        background-color: white;
        color: black;
        width: var(--size);
        border: none;
        border-radius: 2px;
        font-size: calc(var(--size) * 0.5);
        margin: 0;
    }

    .keyboard button.exact {
        background: var(--color-theme-2);
        color: white;
    }

    .keyboard button.missing {
        opacity: 0.5;
    }

    .keyboard button.close {
        border: 2px solid var(--color-theme-2);
    }

    .keyboard button:focus {
        background: var(--color-theme-1);
        color: white;
        outline: none;
    }

    .keyboard button[data-key='enter'],
    .keyboard button[data-key='backspace'] {
        position: absolute;
        bottom: 0;
        width: calc(1.5 * var(--size));
        height: calc(1 / 3 * (100% - 2 * var(--gap)));
        text-transform: uppercase;
        font-size: calc(0.3 * var(--size));
        padding-top: calc(0.15 * var(--size));
    }

    .keyboard button[data-key='enter'] {
        right: calc(50% + 3.5 * var(--size) + 0.8rem);
    }

    .keyboard button[data-key='backspace'] {
        left: calc(50% + 3.5 * var(--size) + 0.8rem);
    }

    .keyboard button[data-key='enter']:disabled {
        opacity: 0.5;
    }

    .restart {
        width: 100%;
        padding: 1rem;
        background: rgba(255, 255, 255, 0.5);
        border-radius: 2px;
        border: none;
    }

    .restart:focus,
    .restart:hover {
        background: var(--color-theme-1);
        color: white;
        outline: none;
    }

    @keyframes wiggle {
        0% {
            transform: translateX(0);
        }
        10% {
            transform: translateX(-2px);
        }
        30% {
            transform: translateX(4px);
        }
        50% {
            transform: translateX(-6px);
        }
        70% {
            transform: translateX(+4px);
        }
        90% {
            transform: translateX(-2px);
        }
        100% {
            transform: translateX(0);
        }
    }
</style>
