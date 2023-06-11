<script lang="ts">
    import {afterUpdate, onMount} from "svelte";

    export let color: string = '#ffffff'

    let colorPickerRef: HTMLInputElement;
    let buttonRef: HTMLButtonElement

    onMount(() => {
        buttonRef.style.backgroundColor = color;
    })

    afterUpdate(() => {
        if (buttonRef) {
            buttonRef.style.backgroundColor = color
        }
    })
</script>

<div class="flex flex-col items-center w-44 p-2">
    <input type="color" hidden bind:this={colorPickerRef} bind:value={color} />
    <button bind:this={buttonRef} class="rounded-full w-10 h-10 mb-1 mt-2" on:click={() => colorPickerRef.click()}>
    </button>
    <input class="bg-gray-300 p-1 rounded mt-1 mb-2 w-24" type="text" bind:value={color} />
</div>

<style></style>