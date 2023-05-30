<script lang="ts">
    import MdMenu from 'svelte-icons/md/MdMenu.svelte';
    import FaAngleDoubleLeft from 'svelte-icons/fa/FaAngleDoubleLeft.svelte';
    import logo from '../../images/logo-yellow.png';
    import CoachNav from "$lib/components/Navbar/CoachNav.svelte";
    import AthleteNav from "$lib/components/Navbar/AthleteNav.svelte";

    let showNav: boolean = false;
    let navView: 'Coach' | 'Athlete' = 'Coach' // TODO: add this to user preferences

</script>

<div class="flex h-full items-center justify-center">
    <div
            class="w-10 text-yellow-lt hover:cursor-pointer hover:text-yellow"
            on:click="{() => (showNav = true)}"
    >
        <MdMenu />
    </div>
</div>
<div class="overlay fixed inset-0 z-40 transform overflow-hidden ease-in-out
    {showNav
        ? 'translate-x-0 opacity-100 transition-opacity duration-100'
        : 'translate-x-full opacity-0 transition-all delay-500'}"
>
    <div
            class="fixed left-0 h-screen z-[250] w-9/12 transform bg-gray-100 shadow-xl transition-all delay-200 duration-200 ease-in-out lg:w-2/12 {showNav
            ? 'translate-x-0'
            : 'translate-x-[-30em]'}"
    >
        <header class="mt-2 flex w-full items-center p-2">
            <img class="m-auto ml-32 h-10" src="{logo}" alt="Coachable Logo" />
            <button
                    class="w-8 text-yellow-lt hover:cursor-pointer hover:text-yellow"
                    on:click="{() => (showNav = false)}"
            >
                <FaAngleDoubleLeft />
            </button>
        </header>
       <div>
           <select class="w-full p-2 bg-gray-200" on:change={(e) => navView = e.target.value}>
               <option>Coach</option>
               <option>Athlete</option>
           </select>
       </div>
        {#if navView === 'Coach'}
            <CoachNav />
        {:else}
            <AthleteNav />
        {/if}
    </div>
</div>
{#if showNav}
    <div class="absolute right-0 top-0 z-[200] h-screen w-2/12 hover:cursor-pointer lg:w-9/12"
         on:click="{() => (showNav = false)}"
    ></div>
{/if}

<style></style>