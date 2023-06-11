<script lang="ts">
    import { isMobile } from '$lib/stores/authStore.js';

    export let borderColor: string = 'border-green';
    export let onClick: (...args) => any;
    export let onContextmenu: (...args) => any;
    export let ID: string = '';
</script>

<div
    on:click="{onClick}"
    on:contextmenu|preventDefault="{onContextmenu}"
    on:dragstart|preventDefault
    id="{ID}"
    class="border-l-2 bg-gray-200 {$isMobile ? 'mx-8' : ''} {borderColor}
        aspect-square overflow-y-auto hover:scale-105 hover:cursor-pointer"
>
    <slot />
</div>

<style></style>
