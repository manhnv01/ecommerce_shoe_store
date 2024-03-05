export class BrandModel {
    id: number;
    name: string;
    slug: string;
    thumbnail: string;
    enabled: boolean;
    
    constructor(data: any) {
        this.id = data.id;
        this.name = data.name;
        this.slug = data.slug;
        this.thumbnail = data.thumbnail;
        this.enabled = data.enabled;
    }
}