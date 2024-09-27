<template>
    <h3>Mejores Destinos: </h3>
    <div class="post-container">
        <PostCard />
        <PostCard />
        <PostCard />
        <PostCard />
        <PostCard />
        <PostCard />
        <PostCard />
    </div>
</template>

<script lang="ts" setup>
import PostCard from './PostCard.vue';
</script>

<style scoped>
.post-container {
    display: grid; /* Use grid for better control */
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); /* Responsive grid: minimum card width 250px */
    gap: 20px; /* Space between the cards */
    width: 100%;
    padding: 20px; /* Padding around the container */
    justify-content: center; /* Center the cards within the container */
}

h3 {
    text-align: center; /* Center the title */
    font-size: 1.8em; /* Increase font size */
    color: #69F0AE; /* Darker color for the heading */
    margin-bottom: 20px; /* Space between heading and content */
}
</style>
