<script lang="ts">
    import { fly, fade } from 'svelte/transition';
    import UserService from '$lib/service/UserService';
    import { userDB } from '../../stores/authStore';
    import { team } from '$lib/stores/teamStore.js';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import IoMdClose from 'svelte-icons/io/IoMdClose.svelte';

    export let showModal: boolean = false;

    let athleteEmail: string = '';
    let athleteName: string = '';
    let success: string = '';
    let error: string = '';
    let loading: boolean = false;

    $: success
        ? setTimeout(() => {
              success = '';
              athleteName = '';
              athleteEmail = '';
          }, 3000)
        : null;

    const sendInvite = async () => {
        error = '';
        loading = true;
        try {
            const inviteUserRequest = {
                coachEmail: $userDB?.email,
                athleteEmail: athleteEmail,
                athleteName: athleteName,
                coachName: $userDB?.username,
                teamId: $team.id,
                coachId: $userDB?.coachData?.id,
                teamName: $team.name,
            };
            const status = await UserService.inviteUser(inviteUserRequest);
            if (status.startsWith('Success')) {
                success = status;
            } else if (status.includes('exists')) {
                error = status;
            }
        } catch (e) {
            console.log(e);
            error = 'The invitation failed to send';
        } finally {
            loading = false;
        }
    };
</script>

{#if showModal}
    <div
        class="fixed bottom-0 top-0 left-0 right-0 z-[200] bg-gray-300 opacity-25"
        on:click="{() => (showModal = false)}"
    ></div>
    <dialog
        open="{showModal}"
        class="fixed z-[201] w-11/12 rounded bg-gray-200 shadow-lg shadow-gray-300 lg:w-3/12"
    >
        <div class="relative flex flex-col p-2">
            <button
                class="absolute right-0 top-0 h-8 w-8 cursor-pointer"
                on:click="{() => (showModal = false)}"
            >
                <span class="text-white">
                    <IoMdClose />
                </span>
            </button>
            <h1
                class="my-3 text-center text-2xl font-semibold tracking-wider text-yellow-lt"
            >
                Invite Athlete
            </h1>
            <input
                type="text"
                placeholder="Name"
                class="my-2 rounded bg-gray-300 p-1 text-textblue"
                bind:value="{athleteName}"
            />
            <input
                type="text"
                placeholder="Email"
                class="my-2 rounded bg-gray-300 p-1 text-textblue"
                bind:value="{athleteEmail}"
            />
            <button
                on:click="{sendInvite}"
                disabled="{!athleteName || !athleteEmail || success || loading}"
                class="my-4 flex cursor-pointer justify-center rounded bg-purple-600 p-2
                       text-white shadow-md shadow-gray-100 hover:bg-purple-500
                       disabled:cursor-not-allowed disabled:bg-purple-900"
            >
                {#if !loading && !success}
                    <p in:fade="{{ duration: 0, delay: 800 }}">Send</p>
                {:else if !success}
                    <LoadingSpinner
                        spinnerColor="fill-white"
                        spinnerBackground="text-purple-600"
                    />
                {:else if success}
                    <span
                        in:fly="{{ x: 50, duration: 1000 }}"
                        out:fade="{{ duration: 600 }}">{success}</span
                    >
                {/if}
            </button>
            {#if error}
                <p class="p-2 text-center text-red">
                    {error}
                </p>
            {/if}
        </div>
    </dialog>
{/if}

<style>
</style>
