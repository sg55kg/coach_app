<script lang="ts">
    import type {LayoutServerData} from "../../../../../../.svelte-kit/types/src/routes/$types";
    import {Program} from "../../../../../lib/classes/program";
    import {onMount, setContext} from "svelte";
    import {writeProgramContext} from "../../../../../lib/components/WriteProgram/contexts/writeProgramContext";

    export let data: LayoutServerData
    let program: Program
    if (data.programDto) {
        const built = Program.build(data.programDto)
        setContext('program', writeProgramContext(built));
        program = built;
    }
    onMount(() => {

    });
</script>

{#if program}
    <slot />
{/if}

<style></style>