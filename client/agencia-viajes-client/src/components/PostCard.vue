<template>
    <div class="post-card">
        <h3 class="post-title">Titulo</h3>
        <p class="post-content">Lorem ipsum, dolor sit amet consectetur adipisicing elit...</p>
        <PostButton class="post-button" />
    </div>
</template>

<script lang="ts" setup>
import PostButton from './PostButton.vue'
</script>

<style scoped>
.post-card {
    padding: 20px;
    border-radius: 15px;
    margin: 10px;
    border: 1px solid #ddd;
    background-color: #f9f9f9; /* Light background for contrast */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* Subtle shadow for depth */
    transition: transform 0.2s, box-shadow 0.2s; /* Smooth transition for hover effect */
}

.post-card:hover {
    transform: scale(1.02); /* Slight zoom effect on hover */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2); /* Enhance shadow on hover */
}

.post-title {
    font-size: 1.5em; /* Larger title */
    color: #333; /* Dark color for text */
    margin-bottom: 10px; /* Space between title and content */
}

.post-content {
    font-size: 1em; /* Standard text size */
    color: #555; /* Slightly lighter color for content */
    line-height: 1.5; /* Increased line height for readability */
}

.post-button {
    margin-top: 15px; /* Space above button */
    color: white; /* Button text color */
    border: none; /* Remove border */
    padding: 10px ;
    border-radius: 5px; /* Rounded corners */
    cursor: pointer; /* Pointer cursor on hover */
    transition: background-color 0.3s; /* Smooth transition for button hover effect */
}

.post-button:hover {
    background-color: #0056b3; /* Darker shade on hover */
}
</style>
