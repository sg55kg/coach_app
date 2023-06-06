<script lang="ts">
    import { onMount } from 'svelte';
    import { authUser, userDB } from '$lib/stores/authStore';
    import { goto } from '$app/navigation';
    import { page } from '$app/stores';
    import type { LayoutData } from './$types';
    import { User } from '$lib/classes/user';

    export let data: LayoutData;

    const { user, userData } = data;

    onMount(async () => {
        if (user && userData) {
            $userDB = User.build(userData);
            $authUser = user;
        }
        if ($userDB) {
            if ($userDB.preferences.defaultHomePage === 'athlete') {
                await goto('/home/athlete');
            } else {
                await goto(
                    `/home/coach/team/${$userDB.coachData.teams[0].id}/athletes`
                );
            }
        }
    });
</script>

<style>
</style>
