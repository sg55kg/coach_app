<script lang="ts">
    import {authUser, userDB} from "$lib/stores/authStore.js";
import AuthHeader from "$lib/components/AuthHeader.svelte";
console.log($userDB.coachData)
</script>

{#if $authUser}
    <AuthHeader />
{/if}
<slot />

<style>

</style>