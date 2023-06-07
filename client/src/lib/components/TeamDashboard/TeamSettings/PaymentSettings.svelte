<script lang="ts">
    import { team } from '$lib/stores/teamStore.js';
    import { onMount } from 'svelte';
    import { TeamService } from '../../../service/TeamService';
    import { StripeStatus } from '../../../classes/team/teamFinance';
    import { userDB } from '../../../stores/authStore';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import Toggle from '$lib/components/shared/layout/Toggle.svelte';

    let stripeAccountCreated: boolean = false;
    let stripeAccount: any = null;
    let loadingStripeConnect: boolean = false;
    let loadingStripeData: boolean = false;
    let showIconOptions: boolean = false;
    let showLogoOptions: boolean = false;
    let iconFileUploader: HTMLInputElement;
    let logoFileUploader: HTMLInputElement;

    let paymentSettingsDisabled: boolean = false;
    $: paymentSettingsDisabled =
        $team.teamFinance.stripeStatus === StripeStatus.ONBOARDING ||
        $team.teamFinance.stripeStatus === StripeStatus.NEW;

    const createStripeConnect = async () => {
        loadingStripeConnect = true;
        const data = {
            coachId: $userDB!.coachData.id,
            teamId: $team.id,
            email: $userDB?.email,
            username: $userDB?.username,
            countryCode: 'US',
            currency: 'USD',
            athleteCap: $team.athletes.length,
        };
        try {
            const res = await TeamService.connectStripeAccount(data);
            if (res.id) {
                $team.teamFinance = res;
            }
            stripeAccountCreated = true;
        } catch (e) {
            console.log(e);
        } finally {
            loadingStripeConnect = false;
        }
    };

    const getStripeRedirectUrl = async () => {
        loadingStripeConnect = true;
        try {
            await TeamService.getStripeRedirectUrl(
                $team.teamFinance.stripeConnectId,
                $team.id
            );
        } catch (e) {
            console.log(e);
        } finally {
            loadingStripeConnect = false;
        }
    };

    onMount(async () => {
        if (
            $team?.teamFinance &&
            $team.teamFinance.stripeStatus === StripeStatus.ONBOARDING
        ) {
            try {
                loadingStripeData = true;
                const res = await TeamService.getStripeAccount(
                    $team.teamFinance.stripeConnectId
                );
                stripeAccount = res;
                console.log(res);
                if (res.details_submitted) {
                    // TODO: save team finance as created and display stripe data on this page
                }
            } catch (e) {
                console.log(e);
            } finally {
                loadingStripeData = false;
            }
        }
    });
</script>

<div class="flex flex-grow flex-col p-3">
    <h1 class="my-2 text-3xl font-semibold tracking-wider">
        Payment Information
    </h1>
    {#if !$team.teamFinance}
        {#if loadingStripeConnect}
            <div class="flex flex-col items-center">
                <LoadingSpinner spinnerColor="fill-yellow" />
                <p>Creating your Stripe account...</p>
            </div>
        {:else}
            <button
                class="font-semibold text-link"
                on:click="{createStripeConnect}"
            >
                Connect with Stripe to enable payments
            </button>
        {/if}
    {:else if $team.teamFinance.stripeStatus === StripeStatus.NEW}
        {#if loadingStripeConnect}
            <div class="flex flex-col items-center">
                <LoadingSpinner spinnerColor="fill-yellow" />
                <p>
                    Loading your account. You will be redirected to Stripe to
                    finish onboarding in just a moment
                </p>
            </div>
        {:else}
            {#if stripeAccountCreated}
                <p class="py-1 text-sm text-green">
                    Success! Click below to setting up your payment information
                    on Stripe
                </p>
            {/if}
            <div class="flex w-full justify-center">
                <button
                    class="m-2 mt-4 rounded border-2 border-textblue bg-gray-100 p-2 px-6 hover:bg-gray-300"
                    on:click="{getStripeRedirectUrl}"
                >
                    Finish set up with Stripe
                </button>
            </div>
        {/if}
    {/if}
    {#if $team.teamFinance}
        <hr class="my-2" />
        <h3 class="text-2xl">Joining {$team.name}</h3>
        <div class="my-3 grid grid-cols-3 gap-3">
            <div class="col-span-1 my-3 flex flex-col">
                <label for="team-athlete-cap-input"
                    >Number of athletes allowed to join {$team.name}</label
                >
                <input
                    type="number"
                    id="team-athlete-cap-input"
                    class="rounded bg-gray-300 p-1 disabled:cursor-not-allowed disabled:bg-gray-200"
                    disabled="{paymentSettingsDisabled}"
                    bind:value="{$team.teamFinance.athleteCap}"
                />
            </div>
            <div class="col-span-1 my-3 flex flex-col items-end">
                <p class="m-0">Request Required to join</p>
                <Toggle
                    disabled="{paymentSettingsDisabled}"
                    checked="{$team.teamFinance.requestRequired}"
                />
            </div>
            <div class="col-span-1 my-3 flex flex-col items-end">
                <p class="m-0">Invite Only</p>
                <Toggle
                    disabled="{paymentSettingsDisabled}"
                    checked="{$team.teamFinance.inviteOnly}"
                />
            </div>
        </div>
        <hr class="my-2" />
        <div>
            {#if stripeAccount}
                <h3 class="text-2xl">Business Profile</h3>
                <div class="grid grid-cols-2 gap-5 bg-gray-100 p-3">
                    <div
                        class="col-span-1 rounded-lg border border-textblue bg-gray-100 p-2"
                    >
                        <h4 class="text-lg text-yellow-lt">Branding</h4>
                        <div class="my-1 flex flex-col">
                            <p>Logo:</p>
                            <div
                                class="relative flex w-full items-center justify-between"
                            >
                                <div class="mx-2 flex flex-col items-start">
                                    <button class="text-link"
                                        >Use my current logo</button
                                    >
                                    <small>- or -</small>
                                    <button
                                        class="text-link"
                                        on:click="{() =>
                                            logoFileUploader.click()}"
                                        >Upload another photo</button
                                    >
                                    <input
                                        type="file"
                                        bind:this="{logoFileUploader}"
                                        hidden
                                        class="rounded bg-gray-300 p-1"
                                        placeholder="Business Logo URL"
                                    />
                                </div>
                                {#if stripeAccount.settings.branding.logo}
                                    <img
                                        src="{stripeAccount.settings.branding
                                            .logo}"
                                        alt="Stripe Business Logo"
                                        class="h-24 w-24 rounded-full"
                                    />
                                {:else}
                                    <div
                                        class="flex h-24 w-24 items-center justify-center rounded-full bg-gray-200 p-1 text-center text-sm"
                                    >
                                        No Photo
                                    </div>
                                {/if}
                            </div>
                        </div>
                        <div class="my-1 flex flex-col">
                            <p>Icon:</p>
                            <div class="flex items-center justify-between">
                                <div class="mx-2 flex flex-col items-start">
                                    <button
                                        class="text-link"
                                        on:click="{() =>
                                            iconFileUploader.click()}"
                                        >{stripeAccount.settings.branding.icon
                                            ? 'Choose a different icon'
                                            : 'Upload icon'}</button
                                    >
                                </div>
                                <input
                                    type="file"
                                    bind:this="{iconFileUploader}"
                                    hidden
                                    class="rounded bg-gray-300 p-1"
                                    placeholder="Business Icon URL"
                                />
                                {#if stripeAccount.settings.branding.icon}
                                    <img
                                        src="{stripeAccount.settings.branding
                                            .icon}"
                                        alt="Stripe Business Icon"
                                        class="h-24 w-24 rounded-full"
                                    />
                                {:else}
                                    <div
                                        class="flex h-24 w-24 items-center justify-center rounded-full bg-gray-200 p-1 text-center text-sm"
                                    >
                                        No Photo
                                    </div>
                                {/if}
                            </div>
                        </div>
                        <div class="my-1 flex flex-col">
                            <p class="my-1">Primary Color:</p>
                            <input
                                type="color"
                                value="{stripeAccount.settings.branding
                                    .primary_color
                                    ? stripeAccount.settings.branding
                                          .primary_color
                                    : '#FFF'}"
                            />
                        </div>
                        <div class="my-1 flex flex-col">
                            <p class="my-1">Secondary Color:</p>
                            <input
                                type="color"
                                value="{stripeAccount.settings.branding
                                    .secondary_color
                                    ? stripeAccount.settings.branding
                                          .secondary_color
                                    : '#FFF'}"
                            />
                        </div>
                    </div>
                    <div
                        class="col-span-1 rounded-lg border border-textblue bg-gray-100 p-2"
                    >
                        <h4 class="text-lg text-yellow-lt">Name here</h4>
                        <div>Bank Name:</div>
                        <div>Support Address:</div>
                        <div>Business URL:</div>
                        <div>Business Email:</div>
                    </div>
                </div>
            {:else if loadingStripeData}
                <div class="flex w-full flex-col items-center p-4">
                    <LoadingSpinner width="w-10" height="h-10" />
                    <p>Fetching your information from Stripe...</p>
                </div>
            {/if}
        </div>
    {/if}
</div>

<style></style>
