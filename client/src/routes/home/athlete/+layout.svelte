<script lang="ts">
    import {userDB} from "$lib/stores/authStore";
    import {authUser} from "$lib/stores/authStore.js";
    import AthleteHeader from "$lib/components/AthleteHeader.svelte";


</script>

<svelte:head>
    <title>{'Welcome ' + $userDB.username}</title>
    <meta name="description" content="Track all of your athletes' progress and stay organized across multiple teams. Use athlete feedback and data from your programs to make better decisions. Write better programs with Coachable." />
</svelte:head>

{#if $authUser}
    <AthleteHeader />
{/if}
<slot />

<style>

</style>