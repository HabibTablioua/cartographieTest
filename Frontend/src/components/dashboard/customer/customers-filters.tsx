import * as React from 'react';
import Card from '@mui/material/Card';
import OutlinedInput from '@mui/material/OutlinedInput';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Button from '@mui/material/Button';
import { MagnifyingGlass as MagnifyingGlassIcon } from '@phosphor-icons/react';
import { FormControl, InputLabel } from '@mui/material';
import axios from 'axios';

interface FormeJuridique {
  id: string;
  nom: string;
}

interface SecteurActivite {
  id: string;
  nom: string;
}

interface CustomersFiltersProps {
  onFilterChange: (filters: Filters) => void;
}

export interface Filters {
  ville: string;
  formeJuridique: string;
  secteurActivite: string;
  denomination: string;
}

export function CustomersFilters({ onFilterChange }: CustomersFiltersProps): React.JSX.Element {
  const [ville, setVille] = React.useState<string>('');
  const [formesJuridiques, setFormesJuridiques] = React.useState<FormeJuridique[]>([]);
  const [secteursActivite, setSecteursActivite] = React.useState<SecteurActivite[]>([]);
  const [villes, setVilles] = React.useState<string[]>([]);
  const [formeJuridique, setFormeJuridique] = React.useState<string>('');
  const [secteurActivite, setSecteurActivite] = React.useState<string>('');
  const [denomination, setDenomination] = React.useState<string>('');

  React.useEffect(() => {
    const fetchFormesJuridiques = async () => {
      try {
        const response = await axios.get('http://localhost:9192/api/formesJuridiques');
        const data: FormeJuridique[] = response.data;
        console.log('Formes Juridiques récupérées:', data);
        setFormesJuridiques(data);
      } catch (error) {
        console.error('Erreur lors de la récupération des formes juridiques', error);
      }
    };

    const fetchSecteursActivite = async () => {
      try {
        const response = await axios.get('http://localhost:9192/api/secteursDactivite');
        const data: SecteurActivite[] = response.data;
        console.log('Secteurs d\'activité récupérés:', data);
        setSecteursActivite(data);
      } catch (error) {
        console.error('Erreur lors de la récupération des secteurs d\'activité', error);
      }
    };

    const fetchVilles = async () => {
      try {
        const response = await axios.get('http://localhost:9192/api/entreprises/villes');
        setVilles(response.data);
      } catch (error) {
        console.error('Erreur lors de la récupération des villes', error);
      }
    };

    fetchFormesJuridiques();
    fetchSecteursActivite();
    fetchVilles();
  }, []);

  const handleCountryChange = (event: SelectChangeEvent<string>) => {
    setVille(event.target.value);
  };

  const handleRegionChange = (event: SelectChangeEvent<string>) => {
    setFormeJuridique(event.target.value);
  };

  const handleActivityChange = (event: SelectChangeEvent<string>) => {
    setSecteurActivite(event.target.value);
  };

  const handleDenominationChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setDenomination(event.target.value);
  };

  const handleSearch = () => {
    const filters: Filters = {
      ville,
      formeJuridique,
      secteurActivite,
      denomination,
    };
    onFilterChange(filters);
  };

  return (
    <Card sx={{ p: 2, display: 'flex', alignItems: 'center' }}>
      <FormControl sx={{ m: 1, minWidth: 120 }}>
        <InputLabel>Ville</InputLabel>
        <Select value={ville} onChange={handleCountryChange}>
          <MenuItem value="">Toutes</MenuItem>
          {villes.map((v) => (
            <MenuItem key={v} value={v}>{v}</MenuItem>
          ))}
        </Select>
      </FormControl>
      <OutlinedInput
        value={denomination}
        onChange={handleDenominationChange}
        placeholder="Saisissez une dénomination"
        sx={{ m: 1, flex: 1 }}
      />
      <FormControl sx={{ m: 1, minWidth: 180 }}>
        <InputLabel>Forme juridique...</InputLabel>
        <Select value={formeJuridique} onChange={handleRegionChange}>
          <MenuItem value="">Toutes</MenuItem>
          {formesJuridiques.map((forme) => (
            <MenuItem key={forme.id} value={forme.id}>{forme.nom}</MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl sx={{ m: 1, minWidth: 200 }}>
        <InputLabel>Secteur d'activité...</InputLabel>
        <Select value={secteurActivite} onChange={handleActivityChange}>
          <MenuItem value="">Tous</MenuItem>
          {secteursActivite.map((secteur) => (
            <MenuItem key={secteur.id} value={secteur.id}>{secteur.nom}</MenuItem>
          ))}
        </Select>
      </FormControl>
      <Button
        variant="contained"
        color="primary"
        onClick={handleSearch}
        sx={{ m: 1 }}
      >
        <MagnifyingGlassIcon fontSize="var(--icon-fontSize-md)" />
      </Button>
    </Card>
  );
}
