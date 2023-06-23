<script lang="ts">
    import Toggle from '$lib/components/shared/layout/Toggle.svelte';
    import { userDB } from '$lib/stores/authStore.js';
    import UserService from '../../../service/UserService';
    import { getContext } from 'svelte';

    const {
        getCurrentProgram,
        getCurrentDay,
        markDayCompleteAsWritten,
        getAthleteProgramLoading,
        getCurrentDayIdx,
        getAthleteId,
    } = getContext('athlete-program');

    let currentProgram = getCurrentProgram();
    let idx = getCurrentDayIdx();
    let currentDay = getCurrentDay();
    let loading = getAthleteProgramLoading();
    let athleteId = getAthleteId();

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

<div class="flex flex-col p-2 lg:flex-row lg:justify-between">
    <h1 class="text-2xl text-textblue">{$currentProgram?.name}</h1>
    <div class="flex">
        <p class="px-1 text-textblue">Pounds</p>
        <Toggle
            checked="{$userDB.preferences.weight === 'kg'}"
            onChange="{toggleWeightPreference}"
        />
        <p class="px-1 text-textblue">Kilograms</p>
    </div>
</div>

<style></style>
