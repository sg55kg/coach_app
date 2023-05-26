<script lang="ts">
    import {Team} from "$lib/classes/team";
    import {TeamService} from "$lib/service/TeamService";
    import FaPen from 'svelte-icons/fa/FaPen.svelte'
    import {team} from "$lib/stores/teamStore";
    import {userDB} from "../../stores/authStore";
    import {StripeStatus} from "$lib/classes/team/teamFinance.js";
    import {onMount} from "svelte";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";

    let newLogoSrc: string = $team.teamLogo
    let newTeamName: string = $team.name
    let newTeamDescription: string = $team.description

    let showNameInput: boolean = false
    let showDescriptionInput: boolean = false
    let stripeAccountCreated: boolean = true
    let stripeAccount: any = null
    let loadingStripeConnect: boolean = false

    $: athleteList = $team ? $team.athletes : []

    let loadingTeam: boolean = true

    const saveTeam = async () => {
        const data = {
            ...$team,
            teamLogo: newLogoSrc,
            description: newTeamDescription,
            name: newTeamName.trim()
        } as Team
        try {
            const teamRes = await TeamService.updateTeam(data)
            $team = teamRes
            athleteList = $team.athletes
            showNameInput = false
            showDescriptionInput = false
            stripeAccountCreated = true
        } catch (e) {
            console.log(e)
        }
    }


    const createStripeConnect = async () => {
        loadingStripeConnect = true
        const data = {
            coachId: $userDB?.coachData?.id,
            teamId: $team.id,
            email: $userDB?.email,
            username: $userDB?.username,
            countryCode: 'US',
            currency: 'USD'
        }
        try {
            const res = await TeamService.connectStripeAccount(data)
            if (res.id) {
                $team.teamFinance = res
            }
        } catch (e) {
            console.log(e)
        } finally {
            loadingStripeConnect = false
        }
    }

    const getStripeRedirectUrl = async () => {
        loadingStripeConnect = true
        try {
            await TeamService.getStripeRedirectUrl($team.teamFinance.stripeConnectId, $team.id)
        } catch (e) {
            console.log(e)
        } finally {
            loadingStripeConnect = false
        }
    }

    onMount(async () => {
        if ($team?.teamFinance && $team.teamFinance.stripeStatus === StripeStatus.ONBOARDING) {
            const res = await TeamService.getStripeAccount($team.teamFinance.stripeConnectId)
            stripeAccount = res
            if (res.details_submitted) {
                // TODO: save team finance as created and display stripe data on this page
            }
        }
    })
</script>

<div class="w-11/12 flex flex-col p-3">
    <div class="w-11/12 lg:w-3/12 m-auto flex flex-col items-center m-2">
        {#if $team.teamLogo && !newLogoSrc}
            <img src={$team.teamLogo} alt="Team logo">
        {:else if !$team.teamLogo && !newLogoSrc}
            <div class="text-xl font-bold">No Logo</div>
        {:else if newLogoSrc}
            <img src={newLogoSrc} alt="New logo">
        {/if}
        <input class="my-2 p-1 bg-gray-300 w-full" type="text" placeholder={$team.teamLogo ? $team.teamLogo : 'Paste an image URL'} bind:value={newLogoSrc}>
        {#if newLogoSrc !== $team.teamLogo}
            <button on:click={saveTeam} class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                Save
            </button>
        {/if}
    </div>
    <div class="flex flex-col items-start m-2">
        {#if !showNameInput}
            <div class="flex flex-col items-center">
                <h4 class="font-medium text-lg">Team Name:</h4>
                <div class="flex">
                    <h4>{$team.name}</h4>
                    <button title="Edit team name" class="h-4 mx-2 text-textblue" on:click={() => showNameInput = !showNameInput}>
                        <FaPen />
                    </button>
                </div>
            </div>
        {:else}
            <input class="w-4/12 bg-gray-300 p-1 text-textblue" bind:value={newTeamName} type="text">
            <div class="w-4/12">
                <button on:click={() => showNameInput = false} class="p-1 px-5 m-1 rounded bg-gray-100 hover:bg-gray-300 text-red-shade">
                    Cancel
                </button>
                {#if newTeamName !== $team.name}
                    <button on:click={saveTeam} class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                        Save
                    </button>
                {/if}
            </div>
        {/if}
    </div>
    <div class="flex flex-col text-md m-2">
        <div>
            <h4 class="font-medium text-lg">Description:</h4>
        </div>
        {#if !showDescriptionInput}
            <div class="flex justify-start items-center">
                <p><i>{$team.description}</i></p>
                <button title="Edit description" class="h-4 mx-2 text-textblue" on:click={() => showDescriptionInput = !showDescriptionInput}>
                    <FaPen />
                </button>
            </div>
        {:else}
            <textarea class="w-6/12 bg-gray-300 text-textblue" bind:value={newTeamDescription}></textarea>
            <div class="w-6/12">
                <button on:click={() => showDescriptionInput = false} class="p-1 px-5 m-1 rounded bg-gray-100 hover:bg-gray-300 text-red-shade">
                    Cancel
                </button>
                {#if newTeamDescription !== $team.description}
                    <button on:click={saveTeam} class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                        Save
                    </button>
                {/if}
            </div>
        {/if}
    </div>
<!--    <div>-->
<!--        <button class="p-2 px-6 border-textblue border-2 rounded bg-gray-100 hover:bg-gray-300 m-2 mt-4">-->
<!--            Invite Athlete-->
<!--        </button>-->
<!--    </div>-->
    <hr class="my-2">
    <div>
        <h3 class="text-xl font-semibold tracking-wider my-1">Payment Information</h3>
        {#if !$team.teamFinance}
            {#if loadingStripeConnect}
                <div class="flex flex-col items-center">
                    <LoadingSpinner spinnerColor="fill-yellow" />
                    <p>Creating your Stripe account...</p>
                </div>
            {:else}
                <button class="text-link font-semibold" on:click={createStripeConnect}>
                    Connect with Stripe to enable payments
                </button>
            {/if}
        {:else if $team.teamFinance.stripeStatus === StripeStatus.NEW}
            {#if loadingStripeConnect}
                <div class="flex flex-col items-center">
                    <LoadingSpinner spinnerColor="fill-yellow" />
                    <p>Loading your account. You will be redirected to Stripe to finish onboarding in just a moment</p>
                </div>
            {:else}
                {#if stripeAccountCreated}
                    <p class="text-green py-1 text-sm">Success! Click below to setting up your payment information on Stripe</p>
                {/if}
                <button class="text-link font-semibold" on:click={getStripeRedirectUrl}>
                    Finish set up with Stripe
                </button>
            {/if}
        {/if}
    </div>
</div>

<style>

</style>