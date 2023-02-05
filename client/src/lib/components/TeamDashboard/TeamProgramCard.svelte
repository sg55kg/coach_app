<script lang="ts">
   import {Program} from "$lib/classes/program";
   import dayjs from "dayjs";
   import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte'
   import FaPen from 'svelte-icons/fa/FaPen.svelte'
   import {ProgramService} from "$lib/service/ProgramService";
   import {programError, programSuccess} from "$lib/stores/writeProgramStore";
   import ConfirmDelete from "$lib/components/modals/ConfirmDelete.svelte";

   export let program: Program
   export let programs: Program[]

   let showConfirmationModal: boolean = false
   const confirmDeleteMessage = 'Are you sure you want to delete ' + program.name + '?'
   const deleteSubheader = 'This will permanently remove all exercises and data used for tracking athlete performance'

   const deleteProgram = async () => {
       try {
           await ProgramService.deleteProgram(program.id)
           programSuccess.set('Successfully deleted program')
           programs = programs.filter(p => p.id !== program.id)
           showConfirmationModal = false
       } catch (e) {
           console.log(e)
           programError.set('Unable to delete program')
       }
   }
</script>

<div class="m-2 bg-gray-100 p-2">
    <h3 class="text-xl">{program.name}</h3>
    <div>{dayjs(program.startDate).format('ddd MMM D')} - {dayjs(program.endDate).format('ddd MMM D')}</div>
    <div class="flex justify-between w-16">
        <div class="h-5 text-red hover:text-red-shade hover:cursor-pointer" on:click={() => showConfirmationModal = !showConfirmationModal}>
            <FaTrashAlt />
        </div>
        <div class="h-5 hover:text-link-shade hover:cursor-pointer">
            <FaPen />
        </div>
    </div>
</div>
<ConfirmDelete show={showConfirmationModal} deleteFunction={deleteProgram} confirmationMessage={confirmDeleteMessage} subheader={deleteSubheader} />
<style>

</style>