<script lang="ts">
    import FaEllipsisV from 'svelte-icons/fa/FaEllipsisV.svelte';
    import { isMobile } from '$lib/stores/authStore.js';
    import { DisplayProgram } from '../../../classes/program';
    import { ProgramService } from '../../../service/ProgramService';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import { userDB } from '../../../stores/authStore';
    import { goto } from '$app/navigation';
    import MdClose from 'svelte-icons/md/MdClose.svelte';
    import MdSearch from 'svelte-icons/md/MdSearch.svelte';

    let viewFilters: boolean = !$isMobile;
    let searchName: string = '';
    let searchResults: DisplayProgram[] = [];
    let loading: boolean = false;
    let success: boolean = false;

    function debounce(func, timeout = 500) {
        let timer;
        return (...args) => {
            clearTimeout(timer);
            timer = setTimeout(() => {
                func.apply(this, args);
            }, timeout);
        };
    }

    const searchPrograms = async () => {
        if (!searchName) return;
        success = false;
        loading = true;
        try {
            const programResults = await ProgramService.searchProgramsByName(
                $userDB.coachData.id,
                searchName
            );
            searchResults = programResults;
            success = true;
        } catch (e) {
            console.log(e);
        } finally {
            loading = false;
        }
    };

    const processSearch = debounce(() => searchPrograms());

    $: success ? setTimeout(() => (success = false), 2000) : null;
</script>

{#if $isMobile}
    <div
        class="flex h-7 w-1/12 justify-end"
        on:click="{() => (viewFilters = !viewFilters)}"
    >
        <MdSearch />
    </div>
{/if}
{#if !$isMobile}
    <div class="w-full">
        <input
            class="w-full bg-gray-300 p-1"
            placeholder="Type to search programs"
            bind:value="{searchName}"
            on:keyup="{() => processSearch()}"
        />
    </div>
    {#if loading}
        <div
            class="absolute top-0 bottom-0 right-0 left-0 z-[110] bg-gray-300 opacity-25"
        ></div>
        <div
            class="absolute bottom-0 top-0 right-0 left-0 z-[115] flex items-center justify-center"
        >
            <LoadingSpinner
                spinnerColor="fill-yellow"
                height="h-10"
                width="w-10"
            />
        </div>
    {/if}
    {#if searchResults.length > 0}
        <div
            class="absolute top-[10em] bottom-0 right-0 left-0 z-[110]"
            on:click="{() => {
                success = false;
                searchResults = [];
            }}"
        ></div>
        <div
            class="absolute top-[10em] right-0 bottom-0 left-0 z-[115] bg-gray-100 px-6"
        >
            <div class="flex w-full justify-center">
                <button
                    class="text-lg text-red-shade"
                    on:click="{() => {
                        searchResults.length = 0;
                        success = false;
                    }}"
                >
                    Return To Program
                </button>
            </div>
            {#each searchResults as result}
                <!--                <a href={"/home/coach/program/" + result.id} >-->
                <div
                    class="m-3 rounded bg-gray-200 p-3 shadow-md hover:cursor-pointer hover:bg-gray-400"
                    on:click="{async () =>
                        await goto(`/home/coach/program/${result.id}`)}"
                >
                    <h3 class="text-lg font-semibold text-yellow-lt">
                        {result.name}
                    </h3>
                    <p>
                        {result.athleteName
                            ? 'Athlete: ' + result.athleteName
                            : 'Template'}
                    </p>
                    <p>
                        Start Date: {result.startDate
                            ? result.startDate.format('ddd MMMM DD')
                            : 'None'}
                    </p>
                    <p>
                        End Date: {result.endDate
                            ? result.endDate.format('ddd MMMM DD')
                            : 'None'}
                    </p>
                </div>
                <!--                </a>-->
            {/each}
        </div>
    {:else if success && searchResults.length < 1}
        <div
            class="absolute bottom-5 left-10 z-10 flex w-8/12 items-center justify-between border-l-4 border-l-sky-400 bg-gray-200 p-4 shadow-lg shadow-black lg:w-4/12"
        >
            No Results found
            <button
                class="h-8 w-8 rounded-full p-1 text-sky-400 hover:bg-gray-400 hover:text-sky-500"
                on:click="{() => (success = false)}"
            >
                <MdClose />
            </button>
        </div>
    {/if}
{/if}
{#if viewFilters && $isMobile}
    <div
        class="absolute top-[84px] h-[70vh] bg-gray-100 {$isMobile
            ? 'right-0 left-0'
            : 'right-3 left-3'} z-20 flex flex-col"
    >
        <div class="flex w-full justify-end">
            <button
                class="h-10 w-12 px-2"
                on:click="{() => (viewFilters = false)}"
                ><span><MdClose /></span></button
            >
        </div>
        <div class="flex items-center justify-center p-2">
            <input
                type="text"
                class="w-full bg-gray-300 p-1"
                placeholder="Search Programs"
                bind:value="{searchName}"
                on:keyup="{() => processSearch()}"
            />
        </div>
        {#if searchResults.length > 0}
            <div class="flex h-[70vh] flex-col overflow-y-auto pl-4">
                {#each searchResults as result}
                    <div
                        class="mx-1 my-3 rounded bg-gray-200 p-2"
                        on:click="{async () =>
                            await goto(`/home/coach/program/${result.id}`)}"
                    >
                        <h3 class="text-lg text-yellow-lt">{result.name}</h3>
                        <p>
                            {result.athleteName
                                ? 'Athlete: ' + result.athleteName
                                : 'Template'}
                        </p>
                        <p>
                            Start Date: {result.startDate
                                ? result.startDate.format('ddd MMMM DD')
                                : 'None'}
                        </p>
                        <p>
                            End Date: {result.endDate
                                ? result.endDate.format('ddd MMMM DD')
                                : 'None'}
                        </p>
                    </div>
                {/each}
            </div>
        {/if}
        {#if loading}
            <div class="flex h-full w-full items-center justify-center">
                <LoadingSpinner
                    spinnerColor="fill-yellow"
                    height="10"
                    width="10"
                />
            </div>
        {/if}
        {#if success && searchResults.length < 1}
            <p class="w-full text-center text-lg font-semibold">
                No Results Found
            </p>
        {/if}
    </div>
{/if}

<style></style>
