<script lang="ts">

    import {DisplayTeam} from "$lib/classes/team";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import UserService from "$lib/service/userService";
    import {AthleteData, CoachData} from "$lib/classes/user";

    export let team: DisplayTeam

    const joinTeam = async () => {
        if (!$userDB?.athleteData || !$auth0Client) return

        let updatedAthlete = $userDB.athleteData.team === null ?
            { ...$userDB.athleteData, team: team, coach: { id: team.coachId } } as AthleteData :
            { ...$userDB.athleteData, team: null, coachId: null } as AthleteData
        console.log('before server athlete data', updatedAthlete)
        try {
            const res: AthleteData = await UserService.updateAthleteData($auth0Client, updatedAthlete)
            console.log('Updated team athlete response', res)
            userDB.update(prev => {
                prev!.athleteData = res
                prev!.athleteData.coach = { id: team.coachId } as CoachData
                return prev
            })
            if (updatedAthlete.team) {
                team.numAthletes++
                team = team
            } else {
                team.numAthletes--
            }
        } catch (e) {
            console.log(e)
        }
    }
    
</script>

<div class="bg-gray-300 p-2 m-2">
    <div class="flex justify-between">
        <h1 class="font-bold text-xl">{team.name}</h1>
        <p>{team.numAthletes} Athletes</p>
    </div>
    <h2 class="font-bold text-lg">Coach: {team.coachName}</h2>
    <div class="my-2">
        <p>{team.description}</p>
    </div>
    <div class="my-2 flex justify-center">
        <button class="bg-yellow hover:bg-yellow-shade rounded p-1 text-black font-bold" on:click={joinTeam}>
            {!$userDB?.athleteData?.team || $userDB?.athleteData?.team?.id !== team.id ?
                `Join ${team.name}` :
                `Leave ${team.name}`
            }
        </button>
    </div>
</div>

<style>

</style>