<script lang="ts">
    import { createEventDispatcher, onMount } from 'svelte';
    import { Team } from '$lib/classes/team';
    import { auth0Client, userDB } from '$lib/stores/authStore';
    import { TeamService } from '$lib/service/TeamService';

    export let open;
    const dispatch = createEventDispatcher();
    let success: boolean = false;

    let team = new Team();

    const createTeam = async () => {
        if (team.coachId === '' && team.coachName === '') {
            team.coachId = $userDB.coachData.id;
            team.coachName = $userDB.username;
        }

        try {
            const savedTeam = await TeamService.createTeam(team);
            userDB.update(prev => {
                prev?.coachData?.teams.push(savedTeam);
                return prev;
            });
            team = new Team();
            success = true;
        } catch (e) {
            console.log(e);
            success = false;
        }
    };
</script>

{#if open}
    <div
        class="relative z-10"
        aria-labelledby="modal-title"
        role="dialog"
        aria-modal="true"
    >
        <!--
      Background backdrop, show/hide based on modal state.

      Entering: "ease-out duration-300"
        From: "opacity-0"
        To: "opacity-100"
      Leaving: "ease-in duration-200"
        From: "opacity-100"
        To: "opacity-0"
    -->

        <div
            class="fixed inset-0 w-full bg-black bg-opacity-75 transition-opacity"
        ></div>
        <div class="fixed inset-0 z-10 overflow-y-auto">
            <div
                class="flex min-h-full items-center justify-center p-4 text-center sm:items-center sm:p-0"
            >
                <!--
              Modal panel, show/hide based on modal state.

              Entering: "ease-out duration-300"
                From: "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                To: "opacity-100 translate-y-0 sm:scale-100"
              Leaving: "ease-in duration-200"
                From: "opacity-100 translate-y-0 sm:scale-100"
                To: "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
            -->
                <div
                    class="relative flex w-10/12 transform flex-col overflow-hidden rounded-lg bg-gray-200 shadow-2xl transition-all sm:my-4 sm:justify-items-center md:w-6/12 md:text-left"
                >
                    <div class="sm:pb bg-gray-200 px-4 pt-5 pb-4 sm:p-6">
                        <div class="sm:flex md:items-start">
                            <div
                                class="flex-rows w-12/12 mt-3 items-start sm:mt-0 sm:ml-4 md:w-9/12 md:text-left"
                            >
                                <h2
                                    class="text-xl font-bold leading-6 text-white"
                                    id="modal-title"
                                >
                                    Create New Team
                                </h2>
                                {#if success}
                                    <div
                                        class="w-10/12 self-center border-green font-bold text-green"
                                    >
                                        <p>Successfully added team!</p>
                                        <div
                                            class="bg-gray-50 96 mr-4 justify-end px-4 py-3 sm:flex sm:flex-row sm:px-6"
                                        >
                                            <button
                                                type="button"
                                                on:click="{() =>
                                                    dispatch('closeModal')}"
                                                class="mt-3 inline-flex w-full justify-center rounded-md px-4 py-2
                                                text-base font-medium text-green shadow-sm hover:text-green-dark
                                                focus:outline-none focus:ring-2
                                                focus:ring-offset-2 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
                                            >
                                                See your team
                                            </button>
                                        </div>
                                    </div>
                                {/if}
                                <div class="mt-2 flex flex-col text-left">
                                    <small>Name</small>
                                    <input
                                        bind:value="{team.name}"
                                        type="text"
                                        placeholder="Team Name"
                                        class="text-md w-12/12 rounded p-2 font-medium text-gray-shade md:w-4/12"
                                    />
                                </div>
                                <div class="mt-2 flex flex-col text-left">
                                    <small>Description</small>
                                    <textarea
                                        placeholder="Enter a description for your team/Your experience as a coach"
                                        bind:value="{team.description}"
                                        class="text-md w-12/12 rounded p-2 font-medium text-gray-shade"
                                    ></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div
                        class="bg-gray-50 96 mr-4 justify-end px-4 py-3 sm:flex sm:flex-row sm:px-6"
                    >
                        <button
                            class="mt-3 inline-flex w-full justify-center rounded-md border border-gray-300 bg-yellow px-4 py-2 text-base font-medium text-gray-shade shadow-sm hover:bg-yellow-shade focus:outline-none focus:ring-2 focus:ring-offset-2 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
                            on:click="{createTeam}"
                        >
                            Create
                        </button>
                        <button
                            type="button"
                            on:click="{() => dispatch('closeModal')}"
                            class="mt-3 inline-flex w-full justify-center rounded-md border border-gray-300 bg-red px-4 py-2 text-base font-medium text-gray-shade shadow-sm hover:bg-yellow-shade focus:outline-none focus:ring-2 focus:ring-offset-2 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
                        >
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
