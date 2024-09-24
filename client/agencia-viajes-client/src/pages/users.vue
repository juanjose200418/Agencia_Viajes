<template>
    <div class="user-management">
      <h1>User Management</h1>
      
      <!-- Formulario para crear o actualizar usuario -->
      <form @submit.prevent="saveUser">
        <div>
          <label for="firstname">First Name:</label>
          <input v-model="user.firstname" id="firstname" type="text" required />
        </div>
  
        <div>
          <label for="lastname">Last Name:</label>
          <input v-model="user.lastname" id="lastname" type="text" required />
        </div>
  
        <div>
          <label for="email">Email:</label>
          <input v-model="user.email" id="email" type="email" required />
        </div>
  
        <div>
          <label for="phone">Phone:</label>
          <input v-model="user.phone" id="phone" type="tel" required />
        </div>
  
        <div>
          <label for="address">Address:</label>
          <input v-model="user.address" id="address" type="text" required />
        </div>
  
        <div>
          <label for="role">Role:</label>
          <select v-model="user.role" id="role">
            <option value="ADMIN">Admin</option>
            <option value="USER">User</option>
            <option value="SUPER_ADMIN">Super Admin</option>
          </select>
        </div>
  
        <button type="submit">{{ user.id ? 'Update User' : 'Add User' }}</button>
      </form>
  
      <!-- Mostrar lista de usuarios -->
      <h2>User List</h2>
      <table>
        <thead>
          <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.firstname }}</td>
            <td>{{ user.lastname }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.phone }}</td>
            <td>{{ user.address }}</td>
            <td>{{ user.role }}</td>
            <td>
              <button @click="editUser(user)">Edit</button>
              <button @click="deleteUser(user.id!!)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
  
      <p v-if="error">{{ error }}</p>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent } from 'vue';
  import UserService from '../services/UserService';
  import { User } from '../models/User'; // Ajusta la ruta según tu proyecto
  import { AxiosError } from 'axios'; // Importamos AxiosError para su manejo en errores HTTP
  
  export default defineComponent({
    data() {
      return {
        users: [] as User[],  // Lista de usuarios
        user: {} as User,     // Usuario en el formulario
        error: ''            // Mensaje de error
      };
    },
    async created() {
      this.fetchUsers();
    },
    methods: {
      // Método para manejar los errores
      handleError(error: unknown) {
        if (error instanceof AxiosError && error.response?.data?.message) {
          // Si es un error de Axios con un mensaje del backend
          this.error = error.response.data.message;
        } else if (error instanceof Error) {
          // Si es un error genérico de JavaScript
          this.error = error.message;
        } else {
          // Si no es un tipo de error conocido
          this.error = 'An unknown error occurred';
        }
      },
  
      async fetchUsers() {
        try {
          this.users = await UserService.getAll(); // Obtener todos los usuarios del backend
        } catch (error) {
          this.handleError(error); // Manejar el error
        }
      },
  
      async saveUser() {
        try {
          if (this.user.id) {
            await UserService.update(this.user.id, this.user);
          } else {
            await UserService.create(this.user);
          }
          this.fetchUsers();  // Actualiza la lista después de guardar
          this.resetForm();    // Reinicia el formulario
        } catch (error) {
          this.handleError(error); // Manejar el error
        }
      },
  
      editUser(user: User) {
        this.user = { ...user }; 
      },
  
      async deleteUser(id: number) {
        if (confirm("Are you sure you want to delete this user?")) {
          try {
            await UserService.delete(id);
            this.fetchUsers(); // Actualiza la lista después de eliminar
          } catch (error) {
            this.handleError(error); // Manejar el error
          }
        }
      },
  
      resetForm() {
        this.user = {} as User;  // Reiniciar el formulario
      }
    }
  });
  </script>
  
  <style scoped>
    .user-management {
      max-width: 800px;
      margin: 0 auto;
    }
  
    table {
      width: 100%;
      border-collapse: collapse;
      margin: 20px 0;
    }
  
    th, td {
      padding: 10px;
      border: 1px solid #ddd;
    }
  
    th {
      background-color: #f4f4f4;
    }
  
    form div {
      margin-bottom: 10px;
    }
  </style>
  