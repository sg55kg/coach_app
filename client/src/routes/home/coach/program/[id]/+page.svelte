<script lang="ts">
    import {afterUpdate, getContext, onDestroy, onMount} from 'svelte';
    import { Program } from '$lib/classes/program';
    import { ProgramService } from '$lib/service/ProgramService';
    import ProgramOverview from '$lib/components/WriteProgram/ProgramOverview.svelte';
    import {page} from "$app/stores";

    const { getProgram } = getContext('program');
    let program = getProgram();
    let reloading: boolean = false;

    onMount(() => {

    });

    afterUpdate(async () => {
        if ($page.params.id && $program?.id !== $page.params.id) {
            reloading = true;
            const res = await ProgramService.getProgram($page.params.id);
            $program = res;
            setTimeout(() => (reloading = false), 100);
        }
    });


    onDestroy(() => {
    });
</script>

{#if program && $program?.id && !reloading}
    <ProgramOverview />
{/if}

<style>
    html,
    body,
    p,
    div,
    h3,
    button,
    header {
        -webkit-touch-callout: none;
        -webkit-user-select: none;
    }
</style>
