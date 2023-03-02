<script lang="ts">
    import {userDB} from "$lib/stores/authStore";
    import {TeamService} from "$lib/service/TeamService";
    import {Team} from "$lib/classes/team";
    import AthleteCard from "$lib/components/TeamDashboard/AthleteCard.svelte";
    import type {PageServerData} from "../../../../../../.svelte-kit/types/src/routes/$types";
    import CardLoadingSkeleton from "$lib/components/shared/loading/CardLoadingSkeleton.svelte";
    import TeamSettings from "$lib/components/TeamDashboard/TeamSettings.svelte";
    import TeamProgramCard from "$lib/components/TeamDashboard/TeamProgramCard.svelte";
    import {onMount} from "svelte";
    import FaChevronLeft from 'svelte-icons/fa/FaChevronLeft.svelte'
    import {team} from "$lib/stores/teamStore";

    let activeTab: 'athletes' | 'programs' | 'settings' = 'athletes'
    $: athleteList = $team ? $team.athletes : []
    $: programList = []

    const handleFilterPrograms = (str: string) => {
        if (!$userDB?.coachData?.programs) return
        programList = $userDB.coachData.programs.filter(p => p.name.includes(str))
    }

    const fetchTeam = async () => {
        let pathArr = location.pathname.split('/')
        const teamId = pathArr[pathArr.length-1]
        if ($team !== null && $team.id === teamId) {
            return
        }
        try {
            const teamRes: Team = await TeamService.getTeam(teamId);
            $team = teamRes
            return teamRes
        } catch (e) {
            console.log(e)
        }
    }

    onMount(() => {
        if (!$userDB?.coachData?.programs) return
        programList = $userDB.coachData.programs
    })

</script>

<svelte:head>
    <title>{$team?.name ? $team.name : 'Loading'}</title>
    <meta name="description" content="Dashboard for {$team?.name}. Manage your athletes and write new programs" />
</svelte:head>

<!--<a href="/home/coach/{$userDB?.coachData?.id}" class="font-bold text-textblue mx-4 lg:ml-20 my-4 h-6 flex">-->
<!--    <span class="lg:mx-4 mx-2">-->
<!--        <FaChevronLeft />-->
<!--    </span>-->
<!--    Back to teams-->
<!--</a>-->

{#await fetchTeam()}
    <div class="flex flex-col w-screen">
        <div class="justify-self-center -mt-4 rounded bg-gray-200 flex justify-between items-center p-3">
            <h1 class="font-bold text-3xl text-white">Loading...</h1>
        </div>
        <CardLoadingSkeleton />
        <CardLoadingSkeleton />
        <CardLoadingSkeleton />
    </div>
{:then teamRes}
    {#if team}
        <div class="flex flex-col items-center">
            <div class="bg-gray-200 tracking-widest w-screen -mt-4 flex flex-col lg:flex-row lg:justify-between lg:py-4 items-center p-3">
                <div class="flex items-center">
                    <h1 class="font-bold text-3xl text-white">
                        {$team.name}
                    </h1>
                </div>
                <div>
                    <button on:click={() => activeTab = 'athletes'} class="mx-2 p-2" class:selected={activeTab === 'athletes'}>
                        Athletes
                    </button>
                    <button on:click={() => activeTab = 'programs'} class="mx-2 p-2" class:selected={activeTab === 'programs'}>
                        Programs
                    </button>
                    <button on:click={() => activeTab = 'settings'} class="mx-2 p-2" class:selected={activeTab === 'settings'}>
                        Settings
                    </button>
                </div>
            </div>
            {#if activeTab === 'athletes'}
                <div class="flex w-screen flex-col p-3">
                    {#each athleteList as athlete, idx (athlete.id)}
                        <AthleteCard bind:athlete={athlete} />
                    {/each}
                    <div class="flex justify-center">
                        <a href="/home/coach/create-program">
                            <button class="mt-5 text-black bg-yellow p-2 rounded font-bold hover:bg-yellow-shade duration-300">
                                Add New Program
                            </button>
                        </a>
                    </div>
                </div>
            {:else if activeTab === 'programs'}
                <div class="w-screen flex flex-col lg:p-3">
                    <div>
                        <h2 class="text-center text-2xl tracking-wider font-bold mb-2">All Programs</h2>
                        <div class="flex flex-col lg:flex-row justify-start items-start">
                            <input type="text" class="bg-gray-300 text-textblue p-1 w-2/12" on:input={(e) => handleFilterPrograms(e.target.value)}>

                            <button class="p-1 px-5 border-textblue border-2 mt-2 m-auto lg:m-0 lg:mx-2 rounded bg-gray-100 hover:bg-gray-300">
                                Search
                            </button>
                        </div>
                    </div>
                    <a class="self-center" href="/home/coach/create-program">
                        <button class="mt-5 mb-5 text-black bg-yellow p-2 rounded font-bold hover:bg-yellow-shade duration-300">
                            Add New Program
                        </button>
                    </a>
                    {#each programList as program}
                       <TeamProgramCard program={program} bind:programs={$userDB.coachData.programs} />
                    {/each}
                </div>
            {:else}
               <TeamSettings team={team} />
            {/if}
        </div>
    {/if}
{:catch error}
    <div>
        We're sorry, there was an error fetching your team
    </div>
{/await}

<style>
    .selected {
        font-weight: bold;
    }
</style>