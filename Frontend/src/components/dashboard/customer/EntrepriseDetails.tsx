import React, { useState, useEffect, useCallback } from 'react';
import { Button, Grid, Avatar, TextField, Box, Typography, IconButton } from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';

interface EntrepriseDetailsProps {
  onBackClick: () => void;
  selectedEntrepriseId: string | null;
}

interface Telephone {
  numero: string;
}

interface Fax {
  numero: string;
}

interface Gerant {
  nom: string;
  prenom: string;
}

interface FormData {
  denomination: string;
  capitalSocial: string;
  ice: string;
  identifiantFiscal: string;
  numRegistreCommerce: string;
  numPatente: string;
  numAffiliationCnss: string;
  adresse: string;
  ville: string;
  mail: string;
  siteWeb: string;
  nombreEmployes: string;
  latitude: string;
  longitude: string;
  dateCreation: string;
  dateCessationActivite: string;
  telephones: Telephone[];
  faxes: Fax[];
  gerants: Gerant[];
  logoUrl: string;
  logo: File | null;
}

export function EntrepriseDetails({ onBackClick, selectedEntrepriseId }: EntrepriseDetailsProps): React.JSX.Element {
  const [formData, setFormData] = useState<FormData>({
    denomination: '',
    capitalSocial: '',
    ice: '',
    identifiantFiscal: '',
    numRegistreCommerce: '',
    numPatente: '',
    numAffiliationCnss: '',
    adresse: '',
    ville: '',
    mail: '',
    siteWeb: '',
    nombreEmployes: '',
    latitude: '',
    longitude: '',
    dateCreation: '',
    dateCessationActivite: '',
    telephones: [{ numero: '' }],
    faxes: [{ numero: '' }],
    gerants: [{ nom: '', prenom: '' }],
    logoUrl: '',
    logo: null,
  });

  const [logoPreview, setLogoPreview] = useState<string | null>(null);

  useEffect(() => {
    const fetchEntrepriseDetails = async () => {
      if (selectedEntrepriseId) {
        try {
          const response = await fetch(`http://localhost:9192/api/entreprises/${selectedEntrepriseId}`);
          if (!response.ok) {
            throw new Error('Failed to fetch entreprise details');
          }
          const data = await response.json();
          setFormData(data);
          setLogoPreview(data.logo ? `data:image/png;base64,${data.logo}` : null);
        } catch (error) {
          console.error('Error fetching entreprise details:', error);
        }
      }
    };
    fetchEntrepriseDetails();
  }, [selectedEntrepriseId]);

  const handleChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  }, []);

  const handleArrayChange = useCallback((field: string, index: number) => (e: React.ChangeEvent<HTMLInputElement>) => {
    const updatedArray = [...formData[field]];
    updatedArray[index].numero = e.target.value;
    setFormData(prev => ({ ...prev, [field]: updatedArray }));
  }, [formData]);

  const handleAddItem = useCallback((field: string) => {
    setFormData(prev => ({
      ...prev,
      [field]: [...prev[field], { numero: '' }]
    }));
  }, []);

  const handleRemoveItem = useCallback((field: string, index: number) => () => {
    setFormData(prev => {
      const updatedArray = [...prev[field]];
      updatedArray.splice(index, 1);
      return { ...prev, [field]: updatedArray };
    });
  }, []);

  const handleGerantChange = useCallback((index: number, field: keyof Gerant) => (e: React.ChangeEvent<HTMLInputElement>) => {
    const updatedGerants = [...formData.gerants];
    updatedGerants[index][field] = e.target.value;
    setFormData(prev => ({ ...prev, gerants: updatedGerants }));
  }, [formData.gerants]);

  const handleAddGerant = useCallback(() => {
    setFormData(prev => ({
      ...prev,
      gerants: [...prev.gerants, { nom: '', prenom: '' }]
    }));
  }, []);

  const handleRemoveGerant = useCallback((index: number) => () => {
    setFormData(prev => {
      const updatedGerants = [...prev.gerants];
      updatedGerants.splice(index, 1);
      return { ...prev, gerants: updatedGerants };
    });
  }, []);

  const handleFileChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] || null;
    setFormData(prev => ({ ...prev, logo: file }));
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => setLogoPreview(reader.result as string);
      reader.readAsDataURL(file);
    } else {
      setLogoPreview(null);
    }
  }, []);

  

  const handleEdit = async () => {
    try {
      // Valider le champ Capital Social
      if (isNaN(parseFloat(formData.capitalSocial))) {
        throw new Error('Le champ Capital Social doit être un nombre valide.');
      }
  
      // Créer FormData
      const formDataToSend = new FormData();
  
      // Ajouter les champs texte au FormData
      Object.keys(formData).forEach(key => {
        if (key === 'logo' && formData[key]) {
          formDataToSend.append(key, formData[key] instanceof File ? formData[key] : new Blob([formData[key]], { type: 'text/plain' }));
        } else if (Array.isArray(formData[key as keyof typeof formData])) {
          formDataToSend.append(key, JSON.stringify(formData[key as keyof typeof formData]));
        } else {
          formDataToSend.append(key, formData[key] ?? '');
        }
      });
  
      // Envoyer la requête PATCH
      const response = await fetch(`http://localhost:9192/api/entreprises/${selectedEntrepriseId}`, {
        method: 'PATCH',
        body: formDataToSend,
        headers: {
          // 'Content-Type': 'multipart/form-data' // Pas besoin de spécifier Content-Type ici; FormData gère cela automatiquement
        }
      });
  
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Failed to update entreprise details: ${errorText}`);
      }
  
      const updatedEntreprise = await response.json();
      console.log('Entreprise updated successfully:', updatedEntreprise);
    } catch (error) {
      console.error('Error updating entreprise details:', error);
      alert('Error updating entreprise details: ' + error.message);
    }
  };
  
  
  
  
  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', p: 3 }}>
      <Button variant="contained" onClick={onBackClick} sx={{ mb: 2 }}>
        Back
      </Button>
      <Typography variant="h4" component="h2" sx={{ mb: 3, textAlign: 'center' }}>
        Détails de l'entreprise
      </Typography>
      <Grid container spacing={2}>
        <Grid item xs={12} sx={{ display: 'flex', justifyContent: 'center' }}>
          {logoPreview && (
            <Avatar src={logoPreview} alt="Logo Preview" sx={{ width: 100, height: 100 }} />
          )}
        </Grid>
        {['denomination', 'capitalSocial', 'ice', 'identifiantFiscal', 'numRegistreCommerce', 'numPatente', 'numAffiliationCnss', 'adresse', 'ville', 'mail', 'siteWeb', 'nombreEmployes', 'latitude', 'longitude'].map(field => (
          <Grid item xs={12} sm={6} key={field}>
            <TextField
              label={field.split(/(?=[A-Z])/).join(' ')}
              name={field}
              value={formData[field as keyof FormData]}
              onChange={handleChange}
              fullWidth
              size="small"
            />
          </Grid>
        ))}
        <Grid item xs={12} sm={6}>
          <TextField
            name="dateCreation"
            label="Date de création"
            type="date"
            value={formData.dateCreation}
            onChange={handleChange}
            InputLabelProps={{ shrink: true }}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="dateCessationActivite"
            label="Date de cessation d'activité"
            type="date"
            value={formData.dateCessationActivite}
            onChange={handleChange}
            InputLabelProps={{ shrink: true }}
            fullWidth
          />
        </Grid>
        {formData.telephones.map((telephone, index) => (
          <Grid item xs={12} sm={6} key={`telephone-${index}`}>
            <TextField
              label={`Téléphone ${index + 1}`}
              value={telephone.numero}
              onChange={handleArrayChange('telephones', index)}
              fullWidth
              size="small"
              InputProps={{
                endAdornment: (
                  <IconButton onClick={handleRemoveItem('telephones', index)} color="error">
                    <DeleteIcon />
                  </IconButton>
                )
              }}
            />
          </Grid>
        ))}
        <Grid item xs={12}>
          <Button onClick={() => handleAddItem('telephones')} startIcon={<AddIcon />} variant="outlined" size="small">
            Ajouter un téléphone
          </Button>
        </Grid>
        {formData.faxes.map((fax, index) => (
          <Grid item xs={12} sm={6} key={`fax-${index}`}>
            <TextField
              label={`Fax ${index + 1}`}
              value={fax.numero}
              onChange={handleArrayChange('faxes', index)}
              fullWidth
              size="small"
              InputProps={{
                endAdornment: (
                  <IconButton onClick={handleRemoveItem('faxes', index)} color="error">
                    <DeleteIcon />
                  </IconButton>
                )
              }}
            />
          </Grid>
        ))}
        <Grid item xs={12}>
          <Button onClick={() => handleAddItem('faxes')} startIcon={<AddIcon />} variant="outlined" size="small">
            Ajouter un fax
          </Button>
        </Grid>
        {formData.gerants.map((gerant, index) => (
          <Grid container spacing={2} key={`gerant-${index}`}>
            <Grid item xs={6}>
              <TextField
                label={`Nom du gérant ${index + 1}`}
                value={gerant.nom}
                onChange={handleGerantChange(index, 'nom')}
                fullWidth
                size="small"
                InputProps={{
                  endAdornment: (
                    <IconButton onClick={handleRemoveGerant(index)} color="error">
                      <DeleteIcon />
                    </IconButton>
                  )
                }}
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                label={`Prénom du gérant ${index + 1}`}
                value={gerant.prenom}
                onChange={handleGerantChange(index, 'prenom')}
                fullWidth
                size="small"
              />
            </Grid>
          </Grid>
        ))}
        <Grid item xs={12}>
          <Button onClick={handleAddGerant} startIcon={<AddIcon />} variant="outlined" size="small">
            Ajouter un gérant
          </Button>
        </Grid>
        <Grid item xs={12}>
          <input
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            style={{ display: 'none' }}
            id="logo-upload"
          />
          <label htmlFor="logo-upload">
            <Button variant="outlined" component="span">
              Télécharger un logo
            </Button>
          </label>
        </Grid>
      </Grid>
      <Box sx={{ mt: 3, textAlign: 'center' }}>
        <Button
          variant="contained"
          onClick={handleEdit}
          startIcon={<SaveIcon />}
          sx={{ mr: 2 }}
        >
          Modifier
        </Button>
      </Box>
    </Box>
  );
}

export default EntrepriseDetails;
