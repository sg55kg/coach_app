<script lang="ts">
    import UserService from "../../../service/UserService";
    import {getContext} from "svelte";
    import {userDB} from "../../../stores/authStore";
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte';
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte';
    import {isMobile} from "$lib/stores/authStore.js";
    import Toggle from "$lib/components/shared/layout/Toggle.svelte";
    import {page} from "$app/stores";
    import MdClose from 'svelte-icons/md/MdClose.svelte';
    import dayjs from "dayjs";
    import AthleteWeeklyChart from "$lib/components/AthleteProgram/charts/AthleteWeeklyChart.svelte";

    const {
        getCurrentProgram,
        getCurrentDay,
        markDayCompleteAsWritten,
        getAthleteProgramLoading,
        getCurrentDayIdx,
        getAthleteId
    } = getContext('athlete-program');

    let currentProgram = getCurrentProgram();
    let idx = getCurrentDayIdx();
    let currentDay = getCurrentDay();
    let loading = getAthleteProgramLoading();
    let athleteId = getAthleteId();

    const incrementDay = () => {
        if ($idx < $currentProgram.days.length - 1) {
            $idx = $idx + 1;
            console.log('idx', $idx);
            $currentDay = $currentProgram.days[$idx];
            console.log('currentDay', $currentDay)
        }
    };

    const decrementDay = () => {
        if ($idx > 0) {
            $idx = $idx - 1;
            $currentDay = $currentProgram.days[$idx];
        }
    };

    const toggleWeightPreference = async () => {
        $loading = true;
        if ($userDB.preferences.weight === 'kg') {
            $userDB.preferences.weight = 'lb';
        } else {
            $userDB.preferences.weight = 'kg';
        }
        try {
            await UserService.updateUserData($userDB);
        } catch (e) {
            console.log(e);
        } finally {
            $loading = false;
        }
    };
</script>

<header class="flex w-full flex-col p-2 relative">
    {#if $page.url.pathname === '/home/athlete/program'}
        <button
                class="absolute right-3 top-2 h-8 w-8 text-lg font-bold lg:w-fit"
                on:click="{() => { $idx = -1; $currentDay = undefined }}"
        >
            <span><MdClose /></span>
        </button>
    {/if}
    <h2 class="text-center text-3xl font-semibold">
        {$currentProgram.name}
    </h2>
    <div class="flex w-full items-center justify-center">
        <div
                class="relative flex items-center justify-center {$isMobile
                    ? 'w-8/12'
                    : 'w-4/12'} p-2"
        >
            <button
                    class="absolute left-0 h-10 w-6 text-yellow"
                    on:click="{decrementDay}"
            >
                <FaAngleLeft />
            </button>
            <h3
                    class="{$currentDay.date.isSame(dayjs(), 'day')
                                ? 'text-yellow'
                                : 'text-textblue'}
                           {$isMobile
                                ? 'text-md'
                                : 'text-2xl'}
                           font-medium"
            >
                {$currentDay.date.format('dddd, MMMM DD')}
            </h3>
            <button
                    class="absolute right-0 h-10 w-6 text-yellow"
                    on:click="{incrementDay}"
            >
                <FaAngleRight />
            </button>
        </div>
    </div>
    <hr class="my-2" />
    <AthleteWeeklyChart />
    <div class="flex justify-center">
        <p class="px-1 {$userDB.preferences.weight === 'kg' ? 'text-textblue' : 'text-yellow'}">Pounds</p>
        <Toggle
                checked="{$userDB.preferences.weight === 'kg'}"
                onChange="{toggleWeightPreference}"
        />
        <p class="px-1 {$userDB.preferences.weight === 'kg' ? 'text-yellow' : 'text-textblue'}">Kilograms</p>
    </div>
</header>

<style></style>