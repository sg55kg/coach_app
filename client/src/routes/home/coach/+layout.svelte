<script lang="ts">
import {authUser} from "$lib/stores/authStore.js";
import AuthHeader from "$lib/components/AuthHeader.svelte";

</script>

{#if $authUser}
    <AuthHeader />
{/if}
<slot />

<style>

</style>