<script setup lang="ts">
  import axios from "axios";
  import {ref} from "vue";
  import {useRouter} from "vue-router";

  const posts = ref([]);

  const router = useRouter();

    posts.value = [];
    axios.get(`/api/posts?page=1&size=10&sort=id,desc`)
        .then(response => {
          posts.value=response.data;
        });


  const page = ref(1);

  const addPage = () => {
    page.value += 1;

    axios.get(`/api/posts?page=${page.value}&size=10&sort=id,desc`)
        .then(response => {
          posts.value = response.data;
        })
  }

  const minusPage = () => {
    if (page.value > 1) {
      page.value -= 1
    }
    axios.get(`/api/posts?page=${page.value}&size=10&sort=id,desc`)
        .then(response => {
          posts.value = response.data;
        })
  }

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id" >
      <div class="title">
        <router-link :to="{name: 'read', params: {postId: post.id}}">
          {{ post.title}}
        </router-link>
      </div>

      <div class="content">
        {{ post.content}}
      </div>
    </li>
  </ul>

  <el-button @click="minusPage()"><</el-button>
  <el-button >{{page}}</el-button>
  <el-button @click="addPage()">></el-button>
</template>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2rem;

    .title {
      a {
        font-size: 1.1rem;
        color: #383838;
        text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      line-height: 1.4;
      color: #7e7e7e;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 8px;
      font-size: 0.78rem;

      .regDate {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}
</style>