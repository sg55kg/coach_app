<script lang="ts">
    import { onMount } from 'svelte';
    import UserService from '$lib/service/userService';
    import ProgramStats from '$lib/components/Stats/ProgramStats.svelte';
    import { AthleteData, AthleteRecord } from '$lib/classes/user/athlete';
    import { userDB } from '$lib/stores/authStore';

    let athlete: AthleteData;
    let records: AthleteRecord[] = [];
    let loadingAthleteData: boolean = true;

    onMount(async () => {
        loadingAthleteData = true;
        const pathArr = window.location.pathname.split('/');
        const athleteId = pathArr[pathArr.length - 1];

        athlete = $userDB.coachData.athletes.find(a => a.id === athleteId);

        try {
            const athleteRecords: AthleteRecord[] =
                await UserService.fetchAthleteRecords(athleteId);
            records = athleteRecords;
        } catch (e) {
            console.log(e);
            return (loadingAthleteData = false);
        }
        try {
            const stats: any = await UserService.fetchAthleteStats(athleteId);
        } catch (e) {
            console.log(e);
            return (loadingAthleteData = false);
        }
        loadingAthleteData = false;
    });
</script>

{#if athlete && records && !loadingAthleteData}
    <ProgramStats athlete="{athlete}" records="{records}" />
{:else if loadingAthleteData}
    <div class="text-center text-xl font-semibold">
        Fetching athlete stats...
    </div>
{:else}
    <div class="text-center text-lg font-semibold">
        Could not retrieve this athlete. Please try refreshing the page or
        navigating away and back.
    </div>
{/if}

<style>
</style>
