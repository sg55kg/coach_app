<script lang="ts">
    import {isMobile} from "$lib/stores/authStore.js";

    export let borderColor: string = 'border-green'
    export let onClick: (...args) => any
    export let onContextmenu: (...args) => any
    export let ID: string = ''
</script>

<div on:click={onClick}
     on:contextmenu|preventDefault={onContextmenu}
     on:dragstart|preventDefault
     id={ID}
     class="bg-gray-200 border-l-2 {$isMobile ? 'mx-8' : ''} {borderColor}
        aspect-square hover:cursor-pointer hover:scale-105 overflow-y-auto"
>
    <slot />
</div>

<style></style>