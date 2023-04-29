<script lang="ts">
    import {Editor} from "@tiptap/core";
    import GoBold from 'svelte-icons/go/GoBold.svelte'
    import GoItalic from 'svelte-icons/go/GoItalic.svelte'
    import FaStrikethrough from 'svelte-icons/fa/FaStrikethrough.svelte'
    import FaListUl from 'svelte-icons/fa/FaListUl.svelte'

    export let editor: Editor

    const toggleBold = () => {
        editor.chain().focus().toggleBold().run()
    }

    const toggleItalic = () => {
        editor.chain().focus().toggleItalic().run()
    }

    const toggleUnderline = () => {
        editor.chain().focus().toggleStrike().run()
    }

    const toggleBulletList = () => {
        editor.chain().focus().toggleBulletList().run()
    }

    const toggleHeading = (level: number) => {
        editor.chain().focus().toggleHeading({ level }).run()
    }

</script>

{#if editor && editor.isEditable}
    <div class="bg-gray-200 flex items-center p-2">
        <button title="ctrl + b/cmd + b" class="{editor.isActive('bold') ? 'text-yellow' : ''} h-5 w-5 mx-2" on:click={toggleBold}>
            <GoBold />
        </button>
        <button title="ctrl + i/cmd + i" class="{editor.isActive('italic') ? 'text-yellow' : ''} h-5 w-5 mx-2" on:click={toggleItalic}>
            <GoItalic />
        </button>
        <button class="{editor.isActive('strike') ? 'text-yellow' : ''} h-5 w-5 mx-2" on:click={toggleUnderline}>
            <FaStrikethrough />
        </button>
        <button class="{editor.isActive('bulletList') ? 'text-yellow' : ''} h-5 w-5 mx-2" on:click={toggleBulletList}>
            <FaListUl />
        </button>
        <button class="{editor.isActive('heading', { level: 1 }) ? 'text-yellow' : ''} h-5 w-5 mx-2 flex items-center" on:click={() => toggleHeading(1)}>
            <i class="font-extrabold">H1</i>
        </button>
        <button class="{editor.isActive('heading', { level: 2 }) ? 'text-yellow' : ''} h-5 w-5 mx-2 flex items-center" on:click={() => toggleHeading(2)}>
            <i class="font-extrabold">H2</i>
        </button>
        <button class="{editor.isActive('heading', { level: 3 }) ? 'text-yellow' : ''} h-5 w-5 mx-2 flex items-center" on:click={() => toggleHeading(3)}>
            <i class="font-extrabold">H3</i>
        </button>
    </div>
{/if}

<style></style>