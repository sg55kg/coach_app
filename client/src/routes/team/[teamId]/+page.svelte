<script lang="ts">
    import type { PageServerData } from './$types';
    import { onMount } from 'svelte';
    import {DisplayTeam, Team} from '../../../lib/classes/team';
    import AuthHeader from '$lib/components/AuthHeader.svelte';
    import RichTextEditor from '$lib/components/shared/texteditor/RichTextEditor.svelte';
    import {goto} from "$app/navigation";
    import {userDB} from "../../../lib/stores/authStore";
    import {AthleteData} from "../../../lib/classes/user/athlete";
    import UserService from "../../../lib/service/UserService";
    import {User} from "../../../lib/classes/user";

    export let data: PageServerData;
    let team: DisplayTeam;
    let displayTeamsError: string = '';

    const joinTeam = async () => {
        if (!$userDB?.athleteData) return;
        displayTeamsError = '';

        let updatedAthlete: AthleteData = JSON.parse(
            JSON.stringify($userDB.athleteData)
        ) as AthleteData;

        if ($userDB.athleteData.team === null) {
            updatedAthlete.team = team as Team;
            updatedAthlete.coachId = team.coachId;
        } else {
            updatedAthlete.team = null;
            updatedAthlete.coachId = null!;
        }

        try {
            const res = await UserService.updateAthleteData(
                updatedAthlete
            );
            userDB.update((prev: User) => {
                prev.athleteData = res;
                prev.athleteData.coachId = team.coachId;
                return prev;
            });
            if (updatedAthlete.team) {
                team.numAthletes++;
                team = team;
            } else {
                team.numAthletes--;
            }
            setTimeout(async () => {
                await goto('/home/athlete');
            }, 1000);
        } catch (e) {
            console.log(e);
            displayTeamsError =
                'Something went wrong attempting to join ' +
                team.name +
                '. Please try again.';
        }
    };

    onMount(async () => {
        team = await DisplayTeam.createFrom(data.team);
    });
</script>

{#if team}
    <div class="h-screen w-screen overflow-y-auto">
        <AuthHeader />
        <div>
            <section
                class="m-8 mt-12 grid grid-cols-6 rounded-lg bg-gray-200 p-4 shadow-md shadow-gray-300"
            >
                <div class="col-span-1 flex justify-center">
                    {#if team.teamLogo}
                        <img
                            src="{team.teamLogo}"
                            class="mt-4 h-24 w-24 rounded-full"
                            alt="{team.name}'s logo"
                        />
                    {:else}
                        <div
                            class="mt-4 h-24 w-24 rounded-full bg-gray-300"
                            id="TODO-REMOVE"
                        ></div>
                    {/if}
                </div>
                <div class="col-span-5">
                    <div class="flex flex w-full flex-col items-start">
                        <h2
                            class="mt-4 text-4xl font-bold uppercase tracking-wider text-yellow-lt"
                        >
                            {team.name}
                        </h2>
                        <h3 class="mt-3 text-xl font-semibold text-textblue">
                            Coach
                        </h3>
                        <h3 class="my-1 text-2xl font-medium tracking-wide">
                            {team.coachName}
                        </h3>
                    </div>
                    <div class="my-2">
                        <RichTextEditor
                            content="{team.description}"
                            editable="{false}"
                        />
                    </div>
                    <div class="flex flex-col items-center py-2">
                        <h3
                            class="text-2xl font-bold tracking-widest text-yellow"
                        >
                            ${team.teamFinance?.teamPrice
                                ? team.teamFinance.teamPrice
                                : 0} / month
                        </h3>
                        <button
                            class="my-2 rounded bg-yellow-shade px-6 py-2 text-lg font-medium text-gray-300 shadow-md shadow-gray-200 hover:bg-yellow"
                            on:click={joinTeam}
                            disabled="{$userDB?.athleteData && $userDB.athleteData.team.id === team.id}"
                        >
                            Join Now
                        </button>
                    </div>
                </div>
            </section>
            <!--        <section>-->
            <!--            <div class="mt-12">-->
            <!--                <h2 class="text-center text-3xl font-semibold">Testimonials</h2>-->
            <!--                <div class="grid grid-cols-2">-->
            <!--                    <div class="col-span-1 bg-gray-200 flex flex-col m-4 p-4 rounded-lg">-->
            <!--                        <h4 class="text-xl">Tagline here</h4>-->
            <!--                        <p>Athlete name?</p>-->
            <!--                        <p>Content</p>-->
            <!--                    </div>-->
            <!--                    <div class="col-span-1 bg-gray-200 flex flex-col m-4 p-4 rounded-lg">-->
            <!--                        <h4 class="text-xl">Tagline here</h4>-->
            <!--                        <p>Athlete name?</p>-->
            <!--                        <p>Content</p>-->
            <!--                    </div>-->
            <!--                </div>-->
            <!--            </div>-->
            <!--            <div class="mt-12 bg-gray-200 p-4 flex flex-col items-center">-->
            <!--                <h2 class="text-3xl font-semibold">Contact {team.name}</h2>-->
            <!--                <h5 class="p-2 text-lg mt-4">Social Media</h5>-->
            <!--                <div class="flex py-2">-->
            <!--                    <div class="rounded-full w-8 h-8 bg-red mx-2"></div>-->
            <!--                    <div class="rounded-full w-8 h-8 bg-green-dark mx-2"></div>-->
            <!--                    <div class="rounded-full w-8 h-8 bg-purple-600 mx-2"></div>-->
            <!--                    <div class="rounded-full w-8 h-8 bg-sky-600 mx-2"></div>-->
            <!--                </div>-->
            <!--                <div class="my-3">-->
            <!--                    <button class="text-link">Message {team.coachName}</button>-->
            <!--                </div>-->
            <!--            </div>-->
            <!--        </section>-->
        </div>
    </div>
{/if}

<style></style>
