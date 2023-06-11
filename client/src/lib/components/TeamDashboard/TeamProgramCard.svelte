<script lang="ts">
    import { DisplayProgram, Program } from '$lib/classes/program';
    import dayjs from 'dayjs';
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte';
    import FaPen from 'svelte-icons/fa/FaPen.svelte';
    import { ProgramService } from '$lib/service/ProgramService';
    import {
        programError,
        programSuccess,
    } from '$lib/stores/writeProgramStore';
    import ConfirmDelete from '$lib/components/modals/ConfirmDelete.svelte';
    import { userDB } from '$lib/stores/authStore';
    import FaRegCopy from 'svelte-icons/fa/FaRegCopy.svelte';

    export let program: DisplayProgram;
    export let programs: Program[];

    let showConfirmationModal: boolean = false;
    const confirmDeleteMessage =
        'Are you sure you want to delete ' + program.name + '?';
    const deleteSubheader =
        'This will permanently remove all exercises and data used for tracking athlete performance';
    $: programAthlete = $userDB?.coachData?.athletes?.find(
        a => a.id === program.athleteId
    );

    const deleteProgram = async () => {
        try {
            await ProgramService.deleteProgram(program.id);
            programSuccess.set('Successfully deleted program');
            programs = programs.filter(p => p.id !== program.id);
            showConfirmationModal = false;
        } catch (e) {
            console.log(e);
            programError.set('Unable to delete program');
        }
    };

    const copyProgram = async () => {
        const programCopy = JSON.parse(JSON.stringify(program));
        programCopy.id = '';
        programCopy.exercises = programCopy.exercises.map(p => ({
            ...p,
            id: '',
            weightCompleted: 0,
            totalRepsCompleted: 0,
            secondsPerSetCompleted: 0,
            comments: [],
            repCompletedArr: [],
            dropSets: p.dropSets.map(d => ({
                ...d,
                id: '',
                weightCompleted: 0,
                totalRepsCompleted: 0,
                secondsPerSetCompleted: 0,
                comments: [],
                repCompletedArr: [],
            })),
        }));
    };
</script>

<div class="mx-0 mt-2 bg-gray-300 p-2 lg:mx-2">
    <div class="flex w-full items-center justify-between">
        <div class="mb-4 flex w-8/12 items-center">
            <h3
                class="block max-w-xs overflow-x-hidden overflow-ellipsis text-ellipsis whitespace-nowrap text-lg lg:text-xl"
            >
                {program.name}
            </h3>
            <div
                class="ml-2 h-4 w-2/12 text-link hover:cursor-pointer hover:text-link-shade"
            >
                <FaPen />
            </div>
        </div>
        <div class="flex w-4/12 justify-end">
            <div
                class="min-h-5 mx-2 h-5 text-red hover:cursor-pointer hover:text-red-shade"
                on:click="{() =>
                    (showConfirmationModal = !showConfirmationModal)}"
            >
                <FaTrashAlt />
            </div>

            <div
                class="min-h-5 mx-2 h-5 text-link hover:cursor-pointer hover:text-link-shade"
            >
                <FaRegCopy />
            </div>
        </div>
    </div>

    <h3>
        Athlete: {programAthlete
            ? programAthlete.name
            : program.athleteId
            ? 'You'
            : 'None'}
    </h3>
    <div>
        {dayjs(program.startDate).format('ddd MMM D')} - {dayjs(
            program.endDate
        ).format('ddd MMM D')}
    </div>
</div>
<ConfirmDelete
    show="{showConfirmationModal}"
    deleteFunction="{deleteProgram}"
    confirmationMessage="{confirmDeleteMessage}"
    subheader="{deleteSubheader}"
/>

<style>
</style>
