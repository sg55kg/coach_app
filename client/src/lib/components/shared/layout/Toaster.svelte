<script lang="ts">
    import MdClose from 'svelte-icons/md/MdClose.svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';

    export let message: string = '';
    export let spinner: boolean = false;
    export let type: 'error' | 'warning' | 'success' | 'info' = 'success';
</script>

<div
    class="sticky bottom-5 left-10 z-10 flex w-8/12 items-center justify-between border-l-4 bg-gray-200 p-4 shadow-2xl shadow-black lg:w-4/12
               {type === 'error'
        ? 'border-l-red-shade'
        : type === 'success'
        ? 'border-l-green'
        : type === 'info'
        ? 'border-l-sky-500'
        : 'border-l-orange'}
               {type === 'error'
        ? 'text-red'
        : type === 'success'
        ? 'text-green'
        : type === 'info'
        ? 'text-sky-500'
        : 'text-orange-shade'}"
>
    <div class="flex items-center">
        <p class="mr-2 lg:mr-3">{message}</p>
        {#if spinner}
            <LoadingSpinner
                spinnerColor="{type === 'error'
                    ? 'fill-red-shade'
                    : type === 'success'
                    ? 'fill-green'
                    : type === 'info'
                    ? 'fill-sky-500'
                    : 'fill-orange'}"
            />
        {/if}
    </div>
    <button
        class="h-8 w-8 rounded-full p-1 hover:bg-gray-400
                   hover:{type === 'error'
            ? 'text-red-shade'
            : type === 'success'
            ? 'text-green-shade'
            : type === 'info'
            ? 'text-sky-600'
            : 'text-orange-shade'}"
        on:click="{() => (message = '')}"
    >
        <MdClose />
    </button>
</div>

<style></style>
