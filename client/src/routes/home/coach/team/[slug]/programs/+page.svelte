<script lang="ts">
    import { team } from '$lib/stores/teamStore.js';
    import ProgramOverview from '$lib/components/WriteProgram/ProgramOverview.svelte';
    import { setContext } from 'svelte';
    import { writeProgramContext } from '../../../../../../lib/components/WriteProgram/contexts/writeProgramContext';
    import { Program } from '../../../../../../lib/classes/program';

    // TODO: add optional query params to set some default values for new programs when navigating from athlete card
    setContext('program', writeProgramContext(new Program()));
</script>

<svelte:head>
    <title>{$team.name + ' Programs'}</title>
    <meta name="description" content="Coachable Program Editor" />
</svelte:head>

<div class="flex flex-col items-center">
    <ProgramOverview />
</div>

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
