<script lang="ts">
    export let show: boolean = false;
    export let deleteFunction: (...args: any[]) => Promise<any>;
    export let confirmationMessage: string =
        'Are you sure you want to delete this?';
    export let subheader: string = '';
</script>

{#if show}
    <div
        class="fixed inset-0 w-full bg-black bg-opacity-50 transition-opacity duration-500"
    >
        <!--    <div class="m-auto bg-gray-100 h-full shadow-xl delay-200 duration-200">-->

        <div
            class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0"
        >
            <div
                class="relative flex w-8/12 transform flex-col overflow-hidden rounded-lg bg-gray-200 shadow-2xl transition-all sm:my-4 sm:justify-items-center md:w-4/12 md:text-left"
            >
                <div class="bg-gray-200 p-6 px-4 lg:pt-6 lg:pb-3">
                    <div class="flex-col text-lg sm:flex md:items-start">
                        <h3>{confirmationMessage}</h3>
                        <small><i>{subheader}</i></small>
                    </div>
                </div>
                <div
                    class="bg-gray-50 mr-3 w-4/12 justify-around self-end px-2 py-3 lg:flex lg:flex-row lg:px-2"
                >
                    <button
                        class="text-link hover:bg-gray-200"
                        on:click="{() => (show = false)}"
                    >
                        Cancel
                    </button>
                    <button
                        class="rounded bg-red p-2 hover:bg-red-shade"
                        on:click="{deleteFunction}"
                    >
                        Confirm
                    </button>
                </div>
            </div>
        </div>
        <!--    </div>-->
    </div>
{/if}

<style></style>
