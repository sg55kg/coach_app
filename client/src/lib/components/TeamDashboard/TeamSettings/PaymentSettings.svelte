<script lang="ts">
    import { team } from '$lib/stores/teamStore.js';
    import {onMount} from "svelte";
    import {TeamService} from "../../../service/TeamService";
    import {StripeStatus} from "../../../classes/team/teamFinance";
    import {userDB} from "../../../stores/authStore";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import Toggle from "$lib/components/shared/layout/Toggle.svelte";

    let stripeAccountCreated: boolean = true;
    let stripeAccount: any = null;
    let loadingStripeConnect: boolean = false;

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
            const res = await TeamService.getStripeAccount(
                $team.teamFinance.stripeConnectId
            );
            stripeAccount = res;
            console.log(res);
            if (res.details_submitted) {
                // TODO: save team finance as created and display stripe data on this page
            }
        }
    });
</script>

<div class="flex flex-col flex-grow p-3">
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
            <button
                class="font-semibold text-link text-lg p-2"
                on:click="{getStripeRedirectUrl}"
            >
                Finish set up with Stripe
            </button>
        {/if}
    {/if}
    {#if $team.teamFinance}
        <div class="my-3 grid grid-cols-3 gap-3">
            <div class="col-span-1 my-3 flex flex-col">
                <label for="team-athlete-cap-input"
                    >Number of athletes allowed to join {$team.name}</label
                >
                <input
                    type="number"
                    id="team-athlete-cap-input"
                    class="rounded bg-gray-300 p-1"
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
    {/if}
</div>

<style></style>
