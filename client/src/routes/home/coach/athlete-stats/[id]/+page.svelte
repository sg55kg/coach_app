<script lang="ts">

    import {onMount} from "svelte";
    import {AthleteData} from "$lib/classes/user";
    import UserService from "$lib/service/userService";
    import {auth0Client} from "$lib/stores/authStore";
    import ProgramStats from "$lib/components/Stats/ProgramStats.svelte";

    let athlete: AthleteData
    let loadingAthleteData: boolean = true

    onMount(async () => {
        loadingAthleteData = true
        const pathArr = window.location.pathname.split('/')
        const athleteId = pathArr[pathArr.length-1]

        try {
            const athleteRes: AthleteData = await UserService.fetchAthleteData($auth0Client!, athleteId)
            athlete = athleteRes
        } catch (e) {
            console.log(e)
        }
        loadingAthleteData = false
    })
</script>

{#if athlete && !loadingAthleteData}
    <ProgramStats athlete={athlete} />
{:else if loadingAthleteData}
    <div class="text-center font-semibold text-xl">
        Fetching athlete stats...
    </div>
{:else}
    <div class="text-center font-semibold text-lg">
        Could not retrieve this athlete. Please try refreshing the page or navigating away and back.
    </div>
{/if}

<style>

</style>