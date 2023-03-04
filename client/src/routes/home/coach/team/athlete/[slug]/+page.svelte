<script lang="ts">
import ViewAthlete from "$lib/components/WriteProgram/ViewAthlete.svelte";
import {AthleteData} from "$lib/classes/user/athlete/index.js";
</script>

<ViewAthlete athlete={new AthleteData()} />

<style></style>