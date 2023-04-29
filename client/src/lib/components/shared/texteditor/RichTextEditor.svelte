<script lang="ts">
    import {Editor} from "@tiptap/core";
    import {onDestroy, onMount} from "svelte";
    import StarterKit from "@tiptap/starter-kit";
    import FixedMenu from "$lib/components/shared/texteditor/FixedMenu.svelte";
    import {BulletList} from "@tiptap/extension-bullet-list";
    import {ListItem} from "@tiptap/extension-list-item";

    export let content = ''
    export let editable: boolean = true

    let editor: Editor
    let element

    onMount(() => {
        editor = new Editor({
            element,
            extensions: [StarterKit],
            content,
            onTransaction: () => {
                editor = editor
            },
        })
        editor.on('update', ({ editor }) => {
            content = editor.getHTML()
        })
        editor.setEditable(editable)
    })

    onDestroy(() => {
        editor.destroy()
    })
</script>

<div class="{editable ? 'border border-gray-400 w-8/12' : 'mr-2 bg-gray-200 border-l-2 border-l-textblue p-2'}">
    <FixedMenu {editor} />
    <div class="element-wrapper" bind:this={element}></div>
</div>

<style>
    .element-wrapper {
        padding: 1rem;
        flex: 1 1 0%;
        resize: both;
        overflow: auto;
    }

    .element-wrapper :global(p:first-of-type) {
        margin-top: 0;
    }

    .element-wrapper :global(p:last-of-type) {
        margin-bottom: 0;
    }

    .element-wrapper > :global(.ProseMirror) {
        outline: 0;
    }

    :global(.element-wrapper > div > ul, menu) {
        list-style: circle;
        padding: 0 1em;
    }

    :global(.element-wrapper > div > ol) {
        list-style: decimal;
        padding: 0 1em;
    }

    :global(.element-wrapper > div > h1) {
        font-size: 2em;
        font-weight: bolder;
    }

    :global(.element-wrapper > div > h2) {
        font-size: 1.5em;
        font-weight: bolder;
    }

    :global(.element-wrapper > div > h3) {
        font-size: 1em;
        font-weight: bolder;
    }
</style>