<template>
  <v-sheet class="mx-auto login-sheet" width="100%">
    <v-form @submit.prevent="submitForm" fast-fail>
      <v-text-field
        v-model="credential.email"
        :rules="emailRules"
        label="Correo Electrónico"
        placeholder="Introduce el correo"
        type="email"
        required
      ></v-text-field>

      <v-text-field
        v-model="credential.password"
        :rules="passwordRules"
        label="Contraseña"
        placeholder="Introduce la contraseña"
        :type="showPassword ? 'text' : 'password'"
        :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
        :aria-label="showPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'"
        :aria-pressed="showPassword"
        required
        @click:append-inner="togglePassword"
        @keydown="detectCapsLock"
        @keyup="detectCapsLock"
        @focus="detectCapsLock"
        @blur="capsLockOn = false"
      ></v-text-field>

      <div v-if="capsLockOn" class="caps-warning" role="status" aria-live="polite">
        ⚠ Bloq Mayús está activado
      </div>

      <v-btn class="mt-2 login-btn" type="submit" :disabled="!formIsValid" block>
        Iniciar Sesión
      </v-btn>

      <v-alert v-if="sessionMessage" type="warning" class="mt-3" prominent>
        {{ sessionMessage }}
      </v-alert>

      <v-alert v-if="errorMessage" type="error" class="mt-3" prominent>
        {{ errorMessage }}
      </v-alert>

      <v-alert v-if="successMessage" type="success" class="mt-3" prominent>
        {{ successMessage }}
      </v-alert>
    </v-form>
  </v-sheet>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';  
import { Credential } from '../models/Credential';
import UserService from '../services/UserService';

const router = useRouter();

const credential = ref<Credential>(new Credential('', ''));
const errorMessage = ref<string | null>(null);
const successMessage = ref<string | null>(null);
const sessionMessage = ref<string | null>(null);
const showPassword = ref(false);
const capsLockOn = ref(false);

onMounted(() => {
  sessionMessage.value = sessionStorage.getItem('authMessage');
  sessionStorage.removeItem('authMessage');
});

// Reglas de validación para el formulario
const emailRules = [
  (v: string) => !!v || 'Correo electrónico es obligatorio',
  (v: string) => /.+@.+\..+/.test(v) || 'El correo electrónico debe ser válido',
];

const passwordRules = [
  (v: string) => !!v || 'Contraseña es obligatoria',
  (v: string) => (v && v.length >= 6) || 'La contraseña debe tener al menos 6 caracteres',
];

// Computed para validar el formulario
const formIsValid = computed(() => {
  return !!credential.value.email && !!credential.value.password;
});

// Método para enviar el formulario
const submitForm = async () => {
  try {
    // Llamada al servicio de autenticación
    const response = await UserService.login(credential.value);
    const { token, user } = response;

    // Almacenar el token y los datos del usuario en localStorage
    localStorage.setItem('authToken', token);
    localStorage.setItem('user', JSON.stringify(user));
    window.dispatchEvent(new Event('auth-changed'));

    // Mostrar mensaje de éxito
    successMessage.value = 'Inicio de sesión exitoso!';
    errorMessage.value = null;
    sessionMessage.value = null;

    router.push('/');
    
  } catch (error: unknown) {
    // Mostrar error
    errorMessage.value = (error as any).message || 'Error al iniciar sesión';
    successMessage.value = null;
  }
};

function togglePassword() {
  showPassword.value = !showPassword.value;
}

function detectCapsLock(event: KeyboardEvent | FocusEvent) {
  if ('getModifierState' in event) {
    capsLockOn.value = event.getModifierState('CapsLock');
  }
}
</script>

<style scoped>
.caps-warning {
  color: #ffb74d;
  font-size: 0.85rem;
  margin-top: -14px;
  margin-bottom: 10px;
}

.login-sheet {
  max-width: 420px;
}

.login-btn {
  min-height: 46px;
}

:deep(.v-field__append-inner) {
  align-items: center;
}
</style>
