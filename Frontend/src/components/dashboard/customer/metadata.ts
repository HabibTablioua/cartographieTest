// src/app/dashboard/customers/metadata.ts
import { config } from '@/config';
import type { Metadata } from 'next';

export const metadata = { title: `Customers | Dashboard | ${config.site.name}` } satisfies Metadata;
