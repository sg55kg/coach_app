<script lang="ts">
    import {createEventDispatcher, onMount} from "svelte";
    import {Team} from "$lib/classes/team";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {TeamService} from "$lib/service/TeamService";

    export let open
    const dispatch = createEventDispatcher()
    let success: boolean = false

    let team = new Team()

    const createTeam = async () => {
        if (!$userDB?.coachData || !$auth0Client) return

        if (team.coachId === '' && team.coachName === '') {
            team.coachId = $userDB.coachData.id
            team.coachName = $userDB.username
        }

        try {
            const savedTeam = await TeamService.createTeam($auth0Client, team)
            userDB.update(prev => {
                prev?.coachData?.teams.push(savedTeam)
                return prev
            })
            team = new Team()
            success = true

        } catch (e) {
            console.log(e)
            success = false
        }
    }

</script>

{#if open}
<div class="relative z-10" aria-labelledby="modal-title" role="dialog" aria-modal="true">
    <!--
      Background backdrop, show/hide based on modal state.

      Entering: "ease-out duration-300"
        From: "opacity-0"
        To: "opacity-100"
      Leaving: "ease-in duration-200"
        From: "opacity-100"
        To: "opacity-0"
    -->

    <div class="fixed inset-0 bg-black bg-opacity-75 transition-opacity w-full"></div>
    <div class="fixed inset-0 z-10 overflow-y-auto">

        <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
            <!--
              Modal panel, show/hide based on modal state.

              Entering: "ease-out duration-300"
                From: "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                To: "opacity-100 translate-y-0 sm:scale-100"
              Leaving: "ease-in duration-200"
                From: "opacity-100 translate-y-0 sm:scale-100"
                To: "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
            -->
            <div class="flex flex-col relative transform overflow-hidden rounded-lg bg-gray-200 sm:justify-items-center md:text-left shadow-2xl transition-all sm:my-4 w-10/12 md:w-6/12">

                <div class="bg-gray-200 px-4 pt-5 pb-4 sm:p-6 sm:pb">
                    <div class="sm:flex md:items-start">
                        <div class="flex-rows mt-3 items-start sm:mt-0 sm:ml-4 md:text-left w-12/12 md:w-9/12">
                            <h2 class="text-xl font-bold leading-6 text-white" id="modal-title">
                                Create New Team
                            </h2>
                            {#if success}
                                <div class="w-10/12 self-center font-bold border-green text-green">
                                    <p>Successfully added team!</p>
                                    <div class="bg-gray-50 px-4 py-3 mr-4 96 sm:flex sm:flex-row sm:px-6 justify-end">
                                                        <button type="button"
                                                              on:click={() => dispatch('closeModal')}
                                                              class="mt-3 inline-flex w-full justify-center rounded-md border border-gray-300 bg-green px-4 py-2 text-base font-medium text-gray-shade shadow-sm hover:bg-yellow-shade focus:outline-none focus:ring-2  focus:ring-offset-2 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                                                                                See your team
                                                        </button>
                                    </div>
                                </div>
                            {/if}
                            <div class="mt-2 flex flex-col text-left">
                                <small>Name</small>
                                <input bind:value={team.name}
                                       type="text"
                                       placeholder="Team Name"
                                       class="text-md text-gray-shade p-2 font-medium rounded w-12/12 md:w-4/12">
                            </div>
                            <div class="mt-2 flex flex-col text-left">
                                <small>Description</small>
                                <textarea placeholder="Enter a description for your team/Your experience as a coach"
                                          bind:value={team.description}
                                          class="text-md text-gray-shade font-medium rounded p-2 w-12/12"
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="bg-gray-50 px-4 py-3 mr-4 96 sm:flex sm:flex-row sm:px-6 justify-end">
                    <button class="mt-3 inline-flex w-full justify-center rounded-md border border-gray-300 bg-yellow px-4 py-2 text-base font-medium text-gray-shade shadow-sm hover:bg-yellow-shade focus:outline-none focus:ring-2 focus:ring-offset-2 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
                            on:click={createTeam}>
                        Create
                    </button>
                    <button type="button"
                            on:click={() => dispatch('closeModal')}
                            class="mt-3 inline-flex w-full justify-center rounded-md border border-gray-300 bg-red px-4 py-2 text-base font-medium text-gray-shade shadow-sm hover:bg-yellow-shade focus:outline-none focus:ring-2  focus:ring-offset-2 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    </div>

</div>
{/if}

<style>

</style>