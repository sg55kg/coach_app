<script lang="ts">
    import {onMount} from "svelte";
    import dayjs from "dayjs";
    import FaRegChartBar from 'svelte-icons/fa/FaRegChartBar.svelte'
    import {userDB} from "$lib/stores/authStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {Program} from "$lib/classes/program";
    import {AthleteData} from "$lib/classes/user/athlete";
    import FaRegPlusSquare from 'svelte-icons/fa/FaRegPlusSquare.svelte'
    import {isMobile} from "$lib/stores/authStore.js";
    import FaPen from 'svelte-icons/fa/FaPen.svelte'
    import {team} from "$lib/stores/teamStore.js";

    export let athlete: AthleteData

    let updateSeverity: 'low' | 'moderate' | 'severe' | 'none' | 'over'
    let lastDay: dayjs = dayjs()
    let editProgramName: boolean = false

    const saveProgramName = async () => {

        try {
            const updatedProgram = {
                ...athlete.currentProgram
            } as Program
            const program: Program = await ProgramService.updateProgram(updatedProgram)
            userDB.update(prev => {
                prev!.coachData!.programs = prev!.coachData!.programs!.map(p => p.id === program.id ? program : p)
                return prev
            })
            athlete.currentProgram = program
            editProgramName = false
        } catch (e) {
            console.log(e)
        }
    }

    onMount(() => {
        if (!athlete.currentProgram) {
            return updateSeverity = 'none'
        }
        const today = dayjs()
        const lastEntered = athlete.currentProgram.days.find(d => d.exercises.length < 1 && !d.isRestDay)
        if (!lastEntered) {
            if (athlete.currentProgram.days.length > 0) {
                if (dayjs(athlete.currentProgram.days[athlete.currentProgram.days.length-1].date).valueOf() <= today.valueOf()) {
                    lastDay = dayjs(athlete.currentProgram.days[athlete.currentProgram.days.length - 1].date)

                    return updateSeverity = 'over'
                }
                return updateSeverity = 'low'
            }

            return updateSeverity = 'none'
        }
        const lastUpdatedDay = dayjs(lastEntered.date)
        lastDay = dayjs(athlete.currentProgram.days[athlete.currentProgram.days.length-1].date)
        if (today.valueOf() >= lastUpdatedDay.valueOf()) {
            return updateSeverity = 'over'
        }
        const diff = dayjs(lastUpdatedDay).diff(today, 'days')

        if ( diff <= 2) {
            updateSeverity = 'severe'
        } else if (diff <= 6) {
            updateSeverity = 'moderate'
        } else {
            updateSeverity = 'low'
        }
    })
</script>

<div class="rounded bg-gray-200 p-2 my-2">
    <div class="flex flex-col items-center lg:flex-row lg:justify-between">
        <a href="/home/coach/team/athlete/{athlete.id}">
            <h2 class="font-semibold text-xl">
                {athlete.name}
            </h2>
        </a>
        <!--{#if !$isMobile && athlete?.currentProgram}-->
        <!--    {dayjs(athlete.currentProgram.startDate).format('ddd MMM DD')} - {dayjs(athlete.currentProgram.endDate).format('ddd MMM DD')}-->
        <!--{/if}-->
    </div>

    {#if athlete.currentProgram}
        <div class="flex flex-col items-center lg:items-start justify-start text-lg">
            {#if !editProgramName}
                <div class="flex justify-center">
                    {#if updateSeverity === 'severe'}
                        <div class="flex items-center">
                            <div class="rounded-full h-3 w-3 bg-red-shade mr-2"></div>
                            <h5 class="text-xl text-center text-red-shade {$isMobile ? 'w-12' : 'w-full'} overflow-hidden">{athlete.currentProgram.name}</h5>
                        </div>
                    {:else if updateSeverity === 'moderate'}
                        <div class="flex items-center">
                            <div class="rounded-full h-3 w-3 bg-yellow mr-2"></div>
                            <h5 class="text-xl text-center text-yellow">{athlete.currentProgram.name}</h5>
                        </div>
                    {:else if updateSeverity === 'low'}
                        <div class="flex items-center">
                            <div class="rounded-full h-3 w-3 bg-green mr-2"></div>
                            <h5 class="text-xl text-center text-green">{athlete.currentProgram.name}</h5>
                        </div>
                    {:else if updateSeverity === 'none'}
                        <p class="m-0 text-base font-normal text-center">{athlete.name} does not have a current program!</p>
                    {:else}
                        <div class="flex items-center">
                            <div class="rounded-full h-3 w-3 bg-orange mr-2"></div>
                            <h5 class="text-xl text-center text-orange max-w-[16em] h-content overflow-hidden truncate">{athlete.currentProgram.name}</h5>
                        </div>
                    {/if}
                </div>
                {#if athlete.currentProgram && $isMobile}
                    {dayjs(athlete.currentProgram.startDate).format('ddd MMM DD')} - {dayjs(athlete.currentProgram.endDate).format('ddd MMM DD')}
                {/if}

            {:else }
                <input class="text-textblue bg-gray-300" bind:value={athlete.currentProgram.name}>
                <button class="mx-2 text-sm hover:bg-textblue hover:text-black px-2" on:click={saveProgramName}>Save</button>
                <button class="mx-2 text-sm text-red hover:text-red-shade hover:bg-gray-100 px-2" on:click={() => editProgramName = false}>Cancel</button>
            {/if}
        </div>
        <div class="flex justify-center lg:justify-start">
            {#if athlete?.currentProgram}
                <a href={`/home/coach/program/${athlete.currentProgram.id}`}>
                    <button class="flex items-center my-2 text-gray-300 hover:bg-yellow-shade bg-yellow px-1 py-px rounded-sm">
                        <div class="h-3 w-3 mx-1"><FaPen /></div>
                        Edit Program
                    </button>
                </a>
            {/if}
        </div>
<!--        <div class="mt-2 flex justify-around">-->
<!--            <div class="h-6 text-link hover:text-link-shade duration-300">-->
<!--                <a class="flex" href={`/home/coach/create-program?athlete=${athlete.id}`}>-->
<!--                    <span class="h-6 mr-1 lg:mr-4"><FaRegPlusSquare /></span> New Program-->
<!--                </a>-->
<!--            </div>-->
<!--            <div class="h-6 text-link hover:text-link-shade duration-300">-->
<!--                <a class="flex h-6" href={`/home/coach/athlete-stats/${athlete.id}`}>-->
<!--                    <span class="h-6 mr-1 lg:mr-4"><FaRegChartBar /></span> Stats-->
<!--                </a>-->
<!--            </div>-->
<!--        </div>-->
    {:else}
        <p class="m-0 text-base font-normal">{athlete.name} does not have a current program</p>
        <div class="mt-2 flex justify-around">
            <div class="h-6 text-link hover:text-link-shade duration-300">
                <a class="flex" href={`/home/coach/team/${$team.id}/programs`}>
                    <p class="h-6 mr-4"><FaRegPlusSquare /></p><p> New Program</p>
                </a>
            </div>
<!--            <div class="h-6 text-link hover:text-link-shade duration-300">-->
<!--                <a class="flex" href={`/home/coach/athlete-stats/${athlete.id}`}>-->
<!--                    <p class="h-6 mr-4"><FaRegChartBar /></p><p> Stats</p>-->
<!--                </a>-->
<!--            </div>-->
        </div>
    {/if}
</div>

<style>

</style>