<script lang="ts">
    import { DisplayTeam, Team } from '$lib/classes/team';
    import { userDB } from '$lib/stores/authStore';
    import UserService from '$lib/service/userService';
    import { CoachData } from '$lib/classes/user/coach';
    import { AthleteData } from '$lib/classes/user/athlete';
    import { goto } from '$app/navigation';

    export let team: DisplayTeam;
    let displayTeamsError: string = '';

    const joinTeam = async () => {
        if (!$userDB?.athleteData) return;
        displayTeamsError = '';

        let updatedAthlete: AthleteData = JSON.parse(
            JSON.stringify($userDB.athleteData)
        ) as AthleteData;

        if ($userDB.athleteData.team === null) {
            updatedAthlete.team = team as Team;
            updatedAthlete.coachId = team.coachId;
        } else {
            updatedAthlete.team = null;
            updatedAthlete.coachId = null;
        }

        try {
            const res: AthleteData = await UserService.updateAthleteData(
                updatedAthlete
            );
            userDB.update(prev => {
                prev!.athleteData = res;
                prev!.athleteData.coach = { id: team.coachId } as CoachData;
                return prev;
            });
            if (updatedAthlete.team) {
                team.numAthletes++;
                team = team;
            } else {
                team.numAthletes--;
            }
            setTimeout(async () => {
                await goto('/home/athlete');
            }, 1000);
        } catch (e) {
            console.log(e);
            displayTeamsError =
                'Something went wrong attempting to join ' +
                team.name +
                '. Please try again.';
        }
    };
</script>

<div class="m-2 bg-gray-300 p-2">
    <div class="flex justify-between">
        <a href="/team/{team.id}">
            <h1 class="text-xl font-bold">{team.name}</h1>
        </a>
        <p>{team.numAthletes} Athletes</p>
    </div>
    <h2 class="text-lg font-bold">Coach: {team.coachName}</h2>
    <div class="my-2">
        <p>{team.description}</p>
    </div>
    <div class="my-2 flex justify-center">
        <button
            class="rounded bg-yellow p-1 font-bold text-black hover:bg-yellow-shade"
            on:click="{joinTeam}"
        >
            {!$userDB?.athleteData?.team ||
            $userDB?.athleteData?.team?.id !== team.id
                ? `Join ${team.name}`
                : `Leave ${team.name}`}
        </button>
    </div>
</div>

<style>
</style>
