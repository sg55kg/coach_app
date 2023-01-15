<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {goto} from "$app/navigation";
    import {ProgramService} from "$lib/service/ProgramService";
    import dayjs from "dayjs";
    import {currentDay, currentProgram} from "$lib/stores/athleteProgramStore";
    import {Program} from "$lib/classes/program";
    import {Day} from "$lib/classes/program/day";

    onMount(async () => {

        if (!$userDB?.athleteData?.currentProgram) {
            return
        }

        try {
            const program = await ProgramService.getProgram($auth0Client!, $userDB.athleteData.currentProgram.id)
            console.log('program Response', program)
            const today = dayjs()

            let day = program.days.find(d => dayjs(d.date).isSame(today, 'days'))
            if (day) {
                currentDay.set(day)
                console.log('currentDay',currentDay)
            }
            currentProgram.set(program)
            console.log(currentProgram)
        } catch (e) {
            console.log(e)
        }

    })

    onDestroy(() => {
        currentProgram.set(new Program())
        currentDay.set(new Day())
    })
</script>

<slot />

<style>

</style>