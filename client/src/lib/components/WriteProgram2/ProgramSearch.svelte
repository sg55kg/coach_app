<script lang="ts">
    import FaEllipsisV from 'svelte-icons/fa/FaEllipsisV.svelte'
    import {isMobile} from "$lib/stores/authStore.js";

    let viewFilters: boolean = !$isMobile
    console.log(viewFilters)
</script>

{#if $isMobile}
    <div class="h-6 w-1/12 flex justify-end" on:click={() => viewFilters = !viewFilters}>
        <FaEllipsisV />
    </div>
{/if}
<!--{#if viewFilters}-->
<!--    <div class="{$isMobile ? 'absolute h-[70vh] bg-red top-24 right-3 left-3 z-20' : 'w-full'} flex flex-col">-->
<!--        <div>-->
<!--            <input type="text" class="bg-gray-300 p-1" placeholder="Search Programs">-->
<!--            <h1 class="text-2xl">fksdjfkldsfjlksd</h1>-->
<!--        </div>-->
<!--    </div>-->
<!--    {#if $isMobile}-->
<!--        <div class="w-screen h-screen z-10" on:click={() => viewFilters = false}></div>-->
<!--    {/if}-->
<!--{/if}-->


<style></style>