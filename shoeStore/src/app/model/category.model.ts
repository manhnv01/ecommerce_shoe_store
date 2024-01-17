export class CategoryModel {
    id: number;
    name: string;
    slug: string;
    enabled: boolean;
    
    constructor(data: any) {
        this.id = data.id;
        this.name = data.name;
        this.slug = data.slug;
        this.enabled = data.enabled;
    }
}