<script lang="ts">

import {team} from "$lib/stores/teamStore.js";
import ProgramOverview from "$lib/components/WriteProgram2/ProgramOverview.svelte";
</script>

<svelte:head>
    <title>{$team.name + ' Programs'}</title>
    <meta name="description" content="Coachable Program Editor" />
</svelte:head>

<div class="flex flex-col items-center">
    <ProgramOverview />
</div>

<style>
    html, body, p, div, h3, button, header {
        -webkit-touch-callout:none;
        -webkit-user-select:none;
    }
</style>