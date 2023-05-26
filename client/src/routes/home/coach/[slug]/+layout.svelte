<script lang="ts">
    import { auth0Client, userDB } from '$lib/stores/authStore';
    import { displayPrograms } from '$lib/stores/programStore';
    import { onMount } from 'svelte';
    import { ProgramService } from '$lib/service/ProgramService';
    import { DisplayProgram } from '$lib/classes/program';
    import {
        programSuccess,
        programError,
    } from '$lib/stores/writeProgramStore.js';
    import UserService from '$lib/service/userService';
    import { goto } from '$app/navigation';
    import type { LayoutServerData } from '../../../../../.svelte-kit/types/src/routes/$types';

    export let data: LayoutServerData;

    if (data.programs && !displayPrograms) {
        $displayPrograms = data.programs.map(p => DisplayProgram.build(p));
    }
</script>

<svelte:head>
    <title>Coach Dashboard</title>
    <meta
        name="description"
        content="View your teams and manage your athletes"
    />
</svelte:head>

<slot />

<style>
</style>
