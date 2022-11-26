<script>
	import Counter from './Counter.svelte';
	import { user } from '../lib/stores/authStore'
	console.log(user)
	export let data
	let userData
	user.subscribe(v => userData = v)
	console.log(userData)
</script>

<svelte:head>
	<title>Home</title>
	<meta name="description" content="Svelte demo app" />
</svelte:head>

<section>
	{#if !userData}
		<h1>Not logged in</h1>
		<a href="/register">
			<button>
				Get Started
			</button>
		</a>
		<small>Already have an account?</small>
		<a href="/login">
			<button>
				Login
			</button>
		</a>

	{:else }
		<h1>Welcome</h1>
		<ul>
			<!--{#each programs as program}-->
			<!--	<li>-->
			<!--		<p>{program.name}</p>-->
			<!--		<a href="/program/{program.id}">View</a>-->
			<!--	</li>-->
			<!--{/each}-->
		</ul>
		<button>Logout</button>
	{/if}
	<Counter />
</section>

<style>
	section {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		flex: 0.6;
	}

	h1 {
		width: 100%;
	}

	.welcome {
		display: block;
		position: relative;
		width: 100%;
		height: 0;
		padding: 0 0 calc(100% * 495 / 2048) 0;
	}

	.welcome img {
		position: absolute;
		width: 100%;
		height: 100%;
		top: 0;
		display: block;
	}
</style>
