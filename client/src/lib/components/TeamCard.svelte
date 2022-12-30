<script lang="ts">

    import type {Team} from "$lib/classes/team";

    export let team: Team
</script>

<div class=" m-2 bg-gray-200 px-4 py-6 lg:w-10/12 shadow-black shadow-md">
    <h1 class="text-lg text-yellow-lt my-1 font-semibold tracking-wider">{team.name}</h1>
    <hr class="text-gray-100">
    <ul class="mt-2 flex flex-col pl-4">
<!--        <p class="m-0 text-textgray text-base font-normal tracking-wide">-->
<!--            -->
<!--        </p>-->
        <li class="text-red">4 programs need a new day in less than two days</li>
        <li class="text-yellow">2 programs need a new day in less than a week</li>
        <li class="text-green">8 programs are up to date</li>
    </ul>
    <div class="mt-2">
        <a class="text-link underline" href={`/home/coach/team/${team.id}`}>View</a>
    </div>
</div>

<style>

</style>