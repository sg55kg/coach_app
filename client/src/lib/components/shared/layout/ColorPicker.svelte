<script lang="ts">
    import { afterUpdate, onMount } from 'svelte';

    export let color: string = '#ffffff';

    let colorPickerRef: HTMLInputElement;
    let buttonRef: HTMLButtonElement;

    onMount(() => {
        buttonRef.style.backgroundColor = color;
    });

    afterUpdate(() => {
        if (buttonRef) {
            buttonRef.style.backgroundColor = color;
        }
    });
</script>

<div class="flex w-44 flex-col items-center p-2">
    <input
        type="color"
        hidden
        bind:this="{colorPickerRef}"
        bind:value="{color}"
    />
    <button
        bind:this="{buttonRef}"
        class="mb-1 mt-2 h-10 w-10 rounded-full"
        on:click="{() => colorPickerRef.click()}"
    >
    </button>
    <input
        class="mt-1 mb-2 w-24 rounded bg-gray-300 p-1"
        type="text"
        bind:value="{color}"
    />
</div>

<style></style>
