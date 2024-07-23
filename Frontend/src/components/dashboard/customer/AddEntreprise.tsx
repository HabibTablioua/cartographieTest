import React, { ChangeEvent, FormEvent, useEffect, useState } from 'react';
import {
    Dialog, DialogActions, DialogContent, DialogTitle, Button, TextField, Stack, IconButton,
    Typography, Select, MenuItem, FormControl, InputLabel, Box
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from 'axios';
import { SelectChangeEvent } from '@mui/material/Select';

interface EntrepriseFormData {
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
    logo: File | null;
    telephones: { numero: string }[];
    faxes: { numero: string }[];
    gerants: { nom: string; prenom: string }[];
    formeJuridique: string;
    secteurActivite: string;
}

interface FormeJuridique {
    id: string;
    nom: string;
}

interface SecteurActivite {
    id: string;
    nom: string;
}

interface AddEntrepriseProps {
    open: boolean;
    handleClose: () => void;
}

const AddEntreprise: React.FC<AddEntrepriseProps> = ({ open, handleClose }) => {
    const [formData, setFormData] = useState<EntrepriseFormData>({
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
        logo: null,
        telephones: [{ numero: '' }],
        faxes: [{ numero: '' }],
        gerants: [{ nom: '', prenom: '' }],
        formeJuridique: '',
        secteurActivite: ''
    });

    const [formesJuridiques, setFormesJuridiques] = useState<FormeJuridique[]>([]);
    const [newFormeJuridique, setNewFormeJuridique] = useState<string>('');
    const [secteursActivite, setSecteursActivite] = useState<SecteurActivite[]>([]);
    const [newSecteurActivite, setNewSecteurActivite] = useState<string>('');
    const [logoPreview, setLogoPreview] = useState<string | null>(null);

    useEffect(() => {
        const fetchFormesJuridiques = async () => {
            try {
                const response = await axios.get('http://localhost:9192/api/formesJuridiques');
                const data = response.data;

                if (Array.isArray(data)) {
                    setFormesJuridiques(data);
                } else {
                    console.error('Les données récupérées ne sont pas un tableau:', data);
                }
            } catch (error) {
                console.error('Erreur lors de la récupération des formes juridiques', error);
            }
        };
        fetchFormesJuridiques();
    }, []);

    useEffect(() => {
        const fetchSecteursActivite = async () => {
            try {
                const response = await axios.get('http://localhost:9192/api/secteursDactivite');
                const data = response.data;

                if (Array.isArray(data)) {
                    setSecteursActivite(data);
                } else {
                    console.error('Les données récupérées ne sont pas un tableau:', data);
                }
            } catch (error) {
                console.error('Erreur lors de la récupération des secteurs d\'activité', error);
            }
        };
        fetchSecteursActivite();
    }, []);

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
        if (e.target.files?.[0]) {
            setFormData({ ...formData, logo: e.target.files[0] });
            setLogoPreview(URL.createObjectURL(e.target.files[0]));
        }
    };

    const handleTelephoneChange = (index: number) => (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        const telephones = [...formData.telephones];
        telephones[index] = { ...telephones[index], [name]: value };
        setFormData({ ...formData, telephones });
    };

    const handleFaxChange = (index: number) => (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        const faxes = [...formData.faxes];
        faxes[index] = { ...faxes[index], [name]: value };
        setFormData({ ...formData, faxes });
    };

    const handleGerantChange = (index: number) => (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        const gerants = [...formData.gerants];
        gerants[index] = { ...gerants[index], [name]: value };
        setFormData({ ...formData, gerants });
    };

    const addTelephoneField = () => {
        setFormData({ ...formData, telephones: [...formData.telephones, { numero: '' }] });
    };

    const addFaxField = () => {
        setFormData({ ...formData, faxes: [...formData.faxes, { numero: '' }] });
    };

    const addGerantField = () => {
        setFormData({ ...formData, gerants: [...formData.gerants, { nom: '', prenom: '' }] });
    };

    const removeTelephoneField = (index: number) => {
        const telephones = [...formData.telephones];
        telephones.splice(index, 1);
        setFormData({ ...formData, telephones });
    };

    const removeFaxField = (index: number) => {
        const faxes = [...formData.faxes];
        faxes.splice(index, 1);
        setFormData({ ...formData, faxes });
    };

    const removeGerantField = (index: number) => {
        const gerants = [...formData.gerants];
        gerants.splice(index, 1);
        setFormData({ ...formData, gerants });
    };

    const handleAddFormeJuridique = async () => {
        try {
            const response = await axios.post('http://localhost:9192/api/formesJuridiques', { nom: newFormeJuridique });
            setFormesJuridiques([...formesJuridiques, response.data]);
            setFormData({ ...formData, formeJuridique: response.data.id });
            setNewFormeJuridique('');
        } catch (error) {
            console.error('Erreur lors de l\'ajout de la nouvelle forme juridique', error);
        }
    };

    const handleAddSecteurActivite = async () => {
        try {
            const response = await axios.post('http://localhost:9192/api/secteursDactivite', { nom: newSecteurActivite });
            setSecteursActivite([...secteursActivite, response.data]);
            setFormData({ ...formData, secteurActivite: response.data.id });
            setNewSecteurActivite('');
        } catch (error) {
            console.error('Erreur lors de l\'ajout du nouveau secteur d\'activité', error);
        }
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const formDataToSend = new FormData();
        Object.entries(formData).forEach(([key, value]) => {
            if (key === 'logo' && value instanceof File) {
                formDataToSend.append(key, value);
            } else if (Array.isArray(value)) {
                value.forEach((item, index) => {
                    formDataToSend.append(`${key}[${index}]`, JSON.stringify(item));
                });
            } else {
                formDataToSend.append(key, value);
            }
        });

        try {
            const response = await axios.post('http://localhost:9192/api/entreprises/add', formDataToSend, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            console.log(response.data);
            handleClose();
        } catch (error) {
            console.error('Erreur lors de l\'ajout de l\'entreprise', error);
        }
    };

    return (
        <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
            <DialogTitle>Ajouter une entreprise</DialogTitle>
            <DialogContent>
                <form onSubmit={handleSubmit}>
                    <Stack spacing={2}>
                        {/* Autres champs du formulaire */}
                        <TextField
                            name="denomination"
                            label="Dénomination"
                            value={formData.denomination}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        {/* Champs pour les téléphones */}
                        {formData.telephones.map((telephone, index) => (
                            <Box key={index} display="flex" alignItems="center">
                                <TextField
                                    name="numero"
                                    label={`Téléphone ${index + 1}`}
                                    value={telephone.numero}
                                    onChange={handleTelephoneChange(index)}
                                    fullWidth
                                   
                                />
                                <IconButton onClick={() => removeTelephoneField(index)}>
                                    <DeleteIcon />
                                </IconButton>
                            </Box>
                        ))}
                        <Button onClick={addTelephoneField} variant="contained" color="primary">
                            Ajouter un téléphone
                        </Button>
                        {/* Champs pour les faxes */}
                        {formData.faxes.map((fax, index) => (
                            <Box key={index} display="flex" alignItems="center">
                                <TextField
                                    name="numero"
                                    label={`Fax ${index + 1}`}
                                    value={fax.numero}
                                    onChange={handleFaxChange(index)}
                                    fullWidth
                                   
                                />
                                <IconButton onClick={() => removeFaxField(index)}>
                                    <DeleteIcon />
                                </IconButton>
                            </Box>
                        ))}
                        <Button onClick={addFaxField} variant="contained" color="primary">
                            Ajouter un fax
                        </Button>
                        {/* Champs pour les gérants */}
                        {formData.gerants.map((gerant, index) => (
                            <Box key={index} display="flex" alignItems="center">
                                <TextField
                                    name="nom"
                                    label={`Nom du gérant ${index + 1}`}
                                    value={gerant.nom}
                                    onChange={handleGerantChange(index)}
                                    fullWidth
                                
                                />
                                <TextField
                                    name="prenom"
                                    label={`Prénom du gérant ${index + 1}`}
                                    value={gerant.prenom}
                                    onChange={handleGerantChange(index)}
                                    fullWidth
                                   
                                />
                                <IconButton onClick={() => removeGerantField(index)}>
                                    <DeleteIcon />
                                </IconButton>
                            </Box>
                        ))}
                        <Button onClick={addGerantField} variant="contained" color="primary">
                            Ajouter un gérant
                        </Button>
                        {/* Forme juridique */}
                        <FormControl fullWidth>
                            <InputLabel id="forme-juridique-label">Forme juridique</InputLabel>
                            <Select
                                labelId="forme-juridique-label"
                                name="formeJuridique"
                                value={formData.formeJuridique}
                                onChange={handleChange}
                                label="Forme juridique"
                            >
                                {formesJuridiques.map((forme) => (
                                    <MenuItem key={forme.id} value={forme.id}>{forme.nom}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <Box display="flex" alignItems="center">
                            <TextField
                                label="Nouvelle Forme Juridique"
                                value={newFormeJuridique}
                                onChange={(e) => setNewFormeJuridique(e.target.value)}
                                fullWidth
                            />
                            <IconButton onClick={handleAddFormeJuridique}>
                                <AddIcon />
                            </IconButton>
                        </Box>
                        {/* Secteur d'activité */}
                        <FormControl fullWidth>
                            <InputLabel id="secteur-activite-label">Secteur d'activité</InputLabel>
                            <Select
                                labelId="secteur-activite-label"
                                name="secteurActivite"
                                value={formData.secteurActivite}
                                onChange={handleChange}
                                label="Secteur d'activité"
                            >
                                {secteursActivite.map((secteur) => (
                                    <MenuItem key={secteur.id} value={secteur.id}>{secteur.nom}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <Box display="flex" alignItems="center">
                            <TextField
                                label="Nouveau Secteur d'activité"
                                value={newSecteurActivite}
                                onChange={(e) => setNewSecteurActivite(e.target.value)}
                                fullWidth
                            />
                            <IconButton onClick={handleAddSecteurActivite}>
                                <AddIcon />
                            </IconButton>
                        </Box>
                        {/* Autres champs du formulaire */}
                        <TextField
                            name="capitalSocial"
                            label="Capital social"
                            value={formData.capitalSocial}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        <TextField
                            name="ice"
                            label="ICE"
                            value={formData.ice}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        <TextField
                            name="identifiantFiscal"
                            label="Identifiant fiscal"
                            value={formData.identifiantFiscal}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        <TextField
                            name="numRegistreCommerce"
                            label="Numéro de registre de commerce"
                            value={formData.numRegistreCommerce}
                            onChange={handleChange}
                            fullWidth
                            
                        />
                        <TextField
                            name="numPatente"
                            label="Numéro de patente"
                            value={formData.numPatente}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        <TextField
                            name="numAffiliationCnss"
                            label="Numéro d'affiliation CNSS"
                            value={formData.numAffiliationCnss}
                            onChange={handleChange}
                            fullWidth
                            
                        />
                       
                       <TextField
                            name="adresse"
                            label="Adresse"
                            value={formData.adresse}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        <TextField
                            name="ville"
                            label="Ville"
                            value={formData.ville}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        <TextField
                            name="mail"
                            label="Mail"
                            value={formData.mail}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        <TextField
                            name="siteWeb"
                            label="Site Web"
                            value={formData.siteWeb}
                            onChange={handleChange}
                            fullWidth
                        />
                        <TextField
                            name="nombreEmployes"
                            label="Nombre d'employés"
                            value={formData.nombreEmployes}
                            onChange={handleChange}
                          
                        />
                        <TextField
                            name="latitude"
                            label="Latitude"
                            value={formData.latitude}
                            onChange={handleChange}
                            fullWidth
                            
                        />
                        <TextField
                            name="longitude"
                            label="Longitude"
                            value={formData.longitude}
                            onChange={handleChange}
                            fullWidth
                           
                        />
                        <TextField
                            name="dateCreation"
                            label="Date de création"
                            type="date"
                            value={formData.dateCreation}
                            onChange={handleChange}
                            InputLabelProps={{ shrink: true }}
                            fullWidth
                         
                        />
                        <TextField
                            name="dateCessationActivite"
                            label="Date de cessation d'activité"
                            type="date"
                            value={formData.dateCessationActivite}
                            onChange={handleChange}
                            InputLabelProps={{ shrink: true }}
                            fullWidth
                        />
                        <Button
                            variant="contained"
                            component="label"
                        >
                            Télécharger le logo
                            <input
                                type="file"
                                hidden
                                onChange={handleFileChange}
                            />
                        </Button>
                        {logoPreview && (
                            <Box mt={2}>
                                <Typography>Prévisualisation du logo :</Typography>
                                <img src={logoPreview} alt="Prévisualisation du logo" style={{ width: '100px', height: '100px' }} />
                            </Box>
                        )}
                    </Stack>
                    <DialogActions>
                        <Button onClick={handleClose} color="secondary">Annuler</Button>
                        <Button type="submit" color="primary">Ajouter</Button>
                    </DialogActions>
                </form>
            </DialogContent>
        </Dialog>
    );
};

export default AddEntreprise;
