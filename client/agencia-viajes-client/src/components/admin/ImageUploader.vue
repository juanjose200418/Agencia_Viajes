<template>
  <v-card variant="tonal" rounded="lg" class="pa-4 image-uploader">
    <div class="text-subtitle-1 font-weight-bold mb-3">{{ title }}</div>

    <v-row>
      <v-col cols="12" md="7">
        <v-text-field
          v-model="urlValue"
          label="URL de imagen"
          placeholder="https://... o /storage/image/..."
          variant="outlined"
          density="comfortable"
          @update:model-value="onUrlChange"
        />

        <v-file-input
          label="Seleccionar imagen"
          variant="outlined"
          density="comfortable"
          accept="image/jpeg,image/png,image/webp"
          prepend-icon="mdi-image-plus"
          :loading="uploading"
          :error-messages="error ? [error] : []"
          @update:model-value="onFileChange"
        />

        <div class="d-flex ga-2">
          <v-btn size="small" variant="tonal" color="error" :disabled="!modelValue" @click="clearImage">
            Quitar imagen
          </v-btn>
        </div>
      </v-col>

      <v-col cols="12" md="5">
        <div class="preview-box">
          <v-img
            v-if="previewUrl"
            :src="normalizedPreviewUrl"
            height="190"
            cover
            rounded="lg"
            lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw=="
          >
            <template v-slot:error>
              <div class="preview-fallback">Imagen no disponible</div>
            </template>
          </v-img>
          <div v-else class="preview-fallback">Sin imagen</div>
        </div>
      </v-col>
    </v-row>
  </v-card>
</template>

<script lang="ts" setup>
import { computed, ref, watch } from 'vue';
import UploadService from '../../services/UploadService';
import { normalizeImageUrl } from '../../utils/images';

const props = defineProps<{
  modelValue?: string;
  title: string;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void;
}>();

const urlValue = ref(props.modelValue || '');
const previewUrl = ref(props.modelValue || '');
const uploading = ref(false);
const error = ref<string | null>(null);

const normalizedPreviewUrl = computed(() => normalizeImageUrl(previewUrl.value));

watch(
  () => props.modelValue,
  (value) => {
    urlValue.value = value || '';
    previewUrl.value = value || '';
  },
);

function onUrlChange(value: string) {
  error.value = null;
  previewUrl.value = value || '';
  emit('update:modelValue', value || '');
}

async function onFileChange(value: File | File[] | undefined) {
  const file = Array.isArray(value) ? value[0] : value;
  if (!file) return;
  error.value = null;
  const allowed = ['image/jpeg', 'image/png', 'image/webp'];
  if (!allowed.includes(file.type)) {
    error.value = 'Solo se permiten JPG, PNG o WEBP';
    return;
  }
  if (file.size > 5 * 1024 * 1024) {
    error.value = 'La imagen no puede superar 5 MB';
    return;
  }
  const localPreview = URL.createObjectURL(file);
  previewUrl.value = localPreview;
  uploading.value = true;
  try {
    const uploadedUrl = await UploadService.uploadImage(file);
    urlValue.value = uploadedUrl;
    previewUrl.value = uploadedUrl;
    emit('update:modelValue', uploadedUrl);
    URL.revokeObjectURL(localPreview);
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al subir imagen';
  } finally {
    uploading.value = false;
  }
}

function clearImage() {
  error.value = null;
  urlValue.value = '';
  previewUrl.value = '';
  emit('update:modelValue', '');
}
</script>

<style scoped>
.image-uploader {
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.preview-box {
  min-height: 190px;
}

.preview-fallback {
  height: 190px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(255,255,255,.08), rgba(255,255,255,.03));
  color: rgba(255,255,255,.72);
}
</style>
